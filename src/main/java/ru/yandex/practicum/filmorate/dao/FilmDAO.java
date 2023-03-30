package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FeatureNotSupportedException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.type.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDAO implements ItemDAO<FilmIdType, Film> {

    public static final String ID_FIELD = "film_id";
    public static final String NAME_FIELD = "film_name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String RELEASE_FIELD = "release_date";
    public static final String DURATION_FIELD = "duration";
    public static final String RANKMPA_FIELD = "RankMPA_id";
    public static final String FILMGENRE_GENRE_ID = "genre_id";
    public static final String FILMGENRE_FILM_ID = "film_id";
    public static final String FILMLIKES_FILM_ID = "film_id";
    public static final String FILMLIKES_USER_ID = "user_id";
    public static final String FILMDIRECTOR_DIRECTOR_ID = "director_id";
    public static final String FILMDIRECTOR_FILM_ID = "film_id";

    public static final String MAX_COUNT = "max_count";
    public static final String LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID = "left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id ";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public static String idNotFoundMsg(FilmIdType _id) {
        return String.format("Не найден фильм с кодом %s!", _id);
    }

    public Film read(final FilmIdType _id) {
        String sqlStatement = String.format("select * from Film " +
                LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                "where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        Film result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), _id);

        return result;
    }

    public List<Film> selectAll() {
        final String sqlStatement = String.format("select * from Film " +
                LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                "order by %1$s", ID_FIELD);
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public Film create(Film _source) {
        final String sqlStatement = String.format("insert into Film (%1$s, %2$s, %3$s, %4$s, %5$s) "
                        + "values ( :%1$s, :%2$s, :%3$s, :%4$s, :%5$s )"
                , NAME_FIELD, DESCRIPTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _source.getName())
                .addValue(DESCRIPTION_FIELD, _source.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(_source.getReleaseDate()))
                .addValue(DURATION_FIELD, _source.getDuration())
                .addValue(RANKMPA_FIELD, _source.getMpa().getId().getValue());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        FilmIdType newId = FilmIdType.of(Objects.requireNonNull(keyHolder.getKey()).longValue());
        updateFilmGenres(newId, _source.getGenres());
        updateFilmDirectors(newId, _source.getDirectors());
        Film result = (Film) _source.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), _source, newId);
        return result;
    }

    public void update(Film _film) {
        final String sqlStatement4Film = String.format("update Film set %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s, %5$s = :%5$s, %6$s = :%6$s where %1$s = :%1$s"
                , ID_FIELD, NAME_FIELD, DESCRIPTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);

        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _film.getId().getValue())
                .addValue(NAME_FIELD, _film.getName())
                .addValue(DESCRIPTION_FIELD, _film.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(_film.getReleaseDate()))
                .addValue(DURATION_FIELD, _film.getDuration())
                .addValue(RANKMPA_FIELD, _film.getMpa().getId().getValue());

        int rowCount = jdbcNamedTemplate.update(sqlStatement4Film, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_film.getId()), this.getClass(), log);
        }
        updateFilmGenres(_film.getId(), _film.getGenres());
        updateFilmDirectors(_film.getId(), _film.getDirectors());

        log.info("Выполнено {}.update({})", this.getClass().getName(), _film);
    }

    private void updateFilmGenres(FilmIdType _filmId, List<Genre> _genres) {
        if (_genres == null) return;
        log.debug("Вызов {}.updateFilmGenres({}, {})", this.getClass().getName(), _filmId, _genres);

        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format(" delete from FilmGenre where %1$s = :%1$s; ", FILMGENRE_FILM_ID));
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMGENRE_FILM_ID, _filmId.getValue());
        if (!_genres.isEmpty()) {
            sbSQL.append(String.format(" insert into FilmGenre (%1$s, %2$s) values ", FILMGENRE_FILM_ID, FILMGENRE_GENRE_ID));
            boolean needComma = false;
            Set<GenreIdType> uniqueValues = new HashSet<>();
            for (Genre genre : _genres) {
                GenreIdType genreId = genre.getId();
                if (uniqueValues.add(genreId)) {
                    String genreParamName = FILMGENRE_GENRE_ID + genreId;
                    sbSQL.append(String.format("%1$s ( :%2$s , :%3$s )", (needComma ? "," : ""), FILMGENRE_FILM_ID, genreParamName));
                    sqlParams.addValue(genreParamName, genreId.getValue());
                    needComma = true;
                }
            }
            sbSQL.append(";");
        }
        jdbcNamedTemplate.update(sbSQL.toString(), sqlParams);
    }

    private void updateFilmDirectors(FilmIdType _filmId, List<Director> _directors) {
        log.debug("Вызов {}.updateFilmDirectors({}, {})", this.getClass().getName(), _filmId, _directors);

        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format(" delete from FilmDirector where %1$s = :%1$s; ", FILMDIRECTOR_FILM_ID));

        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMDIRECTOR_FILM_ID, _filmId.getValue());
        if (_directors != null && !_directors.isEmpty()) {
            sbSQL.append(String.format(" insert into FilmDirector (%1$s, %2$s) values ", FILMDIRECTOR_FILM_ID, FILMDIRECTOR_DIRECTOR_ID));
            boolean needComma = false;
            Set<DirectorIdType> uniqueValues = new HashSet<>();
            for (Director director : _directors) {
                DirectorIdType directorId = director.getId();
                if (uniqueValues.add(directorId)) {
                    String directorParamName = FILMDIRECTOR_DIRECTOR_ID + directorId;
                    sbSQL.append(String.format("%1$s ( :%2$s , :%3$s )", (needComma ? "," : ""), FILMDIRECTOR_FILM_ID, directorParamName));
                    sqlParams.addValue(directorParamName, directorId.getValue());
                    needComma = true;
                }
            }
            sbSQL.append(";");
        }
        jdbcNamedTemplate.update(sbSQL.toString(), sqlParams);
    }

    public void delete(FilmIdType _id) {
        final String sqlStatement = String.format("delete from Film where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), _id);
    }

    private Film makeFilm(ResultSet _rs) throws SQLException {
        log.debug("Вызов {}.makeFilm({})", this.getClass().getName(), _rs);
        final long id = _rs.getLong(ID_FIELD);

        final String sqlStatementGenre = String.format("select * from Genre where %1$s in (select %2$s from FilmGenre where %3$s = :%3$s) order by %1$s"
                , GenreDAO.ID_FIELD, FILMGENRE_GENRE_ID, FILMGENRE_FILM_ID);
        SqlParameterSource sqlParamsGenre = new MapSqlParameterSource().addValue(FILMGENRE_FILM_ID, id);
        List<Genre> genres = jdbcNamedTemplate.query(sqlStatementGenre, sqlParamsGenre, (rs, row) -> GenreDAO.makeGenre(rs));

        final String sqlStatementDirector = String.format("select * from Director where %1$s in (select %2$s from FilmDirector where %3$s = :%3$s) order by %1$s"
                , DirectorDAO.ID_FIELD, FILMDIRECTOR_DIRECTOR_ID, FILMDIRECTOR_FILM_ID);
        SqlParameterSource sqlParamsDirector = new MapSqlParameterSource().addValue(FILMDIRECTOR_FILM_ID, id);
        List<Director> directors = jdbcNamedTemplate.query(sqlStatementDirector, sqlParamsDirector, (rs, row) -> DirectorDAO.makeDirector(rs));

        return Film.builder()
                .id(FilmIdType.of(id))
                .name(_rs.getString(NAME_FIELD))
                .description(_rs.getString(DESCRIPTION_FIELD))
                .releaseDate(_rs.getDate(RELEASE_FIELD).toLocalDate())
                .duration(_rs.getInt(DURATION_FIELD))
                .mpa(RankMPADAO.makeRankMPA(_rs))
                .genres(genres)
                .directors(directors)
                .build();
    }

    public List<Film> getPopular(int _maxCount) {
        final String sqlStatement = String.format(" select top :%1$s film.*, RankMPA.*, Likes.count " +
                "from film " +
                LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                "left outer join (select film_id, count(*) as count from FilmLikes group by film_id) as Likes on Likes.film_id=film.film_id " +
                "order by Likes.count desc, film.film_id", MAX_COUNT);
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(MAX_COUNT, _maxCount);
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.getPopular({})", this.getClass().getName(), _maxCount);

        return result;
    }

    public List<Film> getFilmsByDirector(DirectorIdType _directorId, FilmsByDirectorSortByMode _sortBy) {
        List<Film> result;
        switch (_sortBy) {
            case YEAR:
                String sqlStatementYear = String.format(" select film.*, RankMPA.* " +
                        " from film " +
                        " left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id " +
                        " where film.film_id in (select film_id from filmDirector where filmDirector.director_id= :%1$s) " +
                        " order by  film.release_date, film.film_id", FILMDIRECTOR_DIRECTOR_ID);
                SqlParameterSource sqlParamsYear = new MapSqlParameterSource().addValue(FILMDIRECTOR_DIRECTOR_ID, _directorId.getValue());
                result = jdbcNamedTemplate.query(sqlStatementYear, sqlParamsYear, (rs, row) -> makeFilm(rs));
                break;
            case LIKES:
                String sqlStatementLikes = String.format(" select film.*, RankMPA.*, Likes.count " +
                        " from film " +
                        " left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id " +
                        " left outer join (select film_id, count(*) as count from FilmLikes group by film_id) as Likes on Likes.film_id=film.film_id " +
                        " where film.film_id in (select film_id from filmDirector where filmDirector.director_id= :%1$s) " +
                        " order by Likes.count, film.film_id", FILMDIRECTOR_DIRECTOR_ID);
                SqlParameterSource sqlParamsLikes = new MapSqlParameterSource().addValue(FILMDIRECTOR_DIRECTOR_ID, _directorId.getValue());
                result = jdbcNamedTemplate.query(sqlStatementLikes, sqlParamsLikes, (rs, row) -> makeFilm(rs));
                break;
            default:
                throw new FeatureNotSupportedException(this.getClass(), log);
        }
        if (result.isEmpty())
            throw new KeyNotFoundException(DirectorDAO.idNotFoundMsg(_directorId), this.getClass(), log);
        return result;
    }

    public int getLikesCount(FilmIdType _id) {
        final String sqlStatement = String.format("select count(*) as %2$s from FilmLikes join Film on Film.film_id = FilmLikes.film_id where %1$s = :%1$s", ID_FIELD, MAX_COUNT);
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(ID_FIELD, _id);
        int result;
        try {
            result = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> rs.getInt(MAX_COUNT)));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }
        log.info("Выполнено {}.getLikesCount({})", this.getClass().getName(), _id);
        return result;
    }

    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        final String sqlStatement = String.format("insert into  FilmLikes (%1$s, %2$s) values( :%1$s , :%2$s )", FILMLIKES_FILM_ID, FILMLIKES_USER_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_FILM_ID, _filmId.getValue())
                .addValue(FILMLIKES_USER_ID, _userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_filmId), this.getClass(), log);
        }

        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), _filmId, _userId);
    }

    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        final String sqlStatement = String.format("delete from FilmLikes where %1$s = :%1$s and %2$s = :%2$s", FILMLIKES_FILM_ID, FILMLIKES_USER_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_FILM_ID, _filmId.getValue())
                .addValue(FILMLIKES_USER_ID, _userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_filmId), this.getClass(), log);
        }

        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
    }


}
