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
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.GenreIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDAO implements ItemDAO<FilmIdType, Film> {

    public static final String ID_FIELD = "film_id";
    public static final String NAME_FIELD = "film_name";
    public static final String DESCRIPRTION_FIELD = "description";
    public static final String RELEASE_FIELD = "release_date";
    public static final String DURATION_FIELD = "duration";
    public static final String RANKMPA_FIELD = "RankMPA_id";
    public static final String FILMGENRE_GENRE_ID = "genre_id";
    public static final String FILMGENRE_FILM_ID = "film_id";
    public static final String FILMLIKES_FILM_ID = "film_id";
    public static final String FILMLIKES_USER_ID = "user_id";
    public static final String MAX_COUNT = "max_count";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;
    private final List<String> validColumns = List.of(NAME_FIELD, DESCRIPRTION_FIELD);

    private String idNotFoundMsg(FilmIdType _id) {
        return String.format("Не найден фильм с кодом %s!", _id);
    }

    public Film read(final FilmIdType _id) {
        String sqlStatement = String.format("select * from Film left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id where %1$s = :%1$s", ID_FIELD);
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
        final String sqlStatement = String.format("select * from Film  left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id order by %1$s", ID_FIELD);
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public Film create(Film _source) {
        final String sqlStatement = String.format("insert into Film (%1$s, %2$s, %3$s, %4$s, %5$s) values ( :%1$s, :%2$s, :%3$s, :%4$s, :%5$s )"
                , NAME_FIELD, DESCRIPRTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _source.getName())
                .addValue(DESCRIPRTION_FIELD, _source.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(_source.getReleaseDate()))
                .addValue(DURATION_FIELD, _source.getDuration())
                .addValue(RANKMPA_FIELD, _source.getMpa().getId().getValue())
                .addValue(NAME_FIELD, _source.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        FilmIdType newId = FilmIdType.of(Objects.requireNonNull(keyHolder.getKey()).longValue());
        updateFilmGenres(newId, _source.getGenres());
        Film result = (Film) _source.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), _source, newId);
        return result;
    }

    public void update(Film _film) {
        final String sqlStatement4Film = String.format("update Film set %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s, %5$s = :%5$s, %6$s = :%6$s  where %1$s = :%1$s"
                , ID_FIELD, NAME_FIELD, DESCRIPRTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);

        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _film.getId().getValue())
                .addValue(NAME_FIELD, _film.getName())
                .addValue(DESCRIPRTION_FIELD, _film.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(_film.getReleaseDate()))
                .addValue(DURATION_FIELD, _film.getDuration())
                .addValue(RANKMPA_FIELD, _film.getMpa().getId().getValue())
                .addValue(NAME_FIELD, _film.getName());

        int rowCount = jdbcNamedTemplate.update(sqlStatement4Film, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_film.getId()), this.getClass(), log);
        }
        updateFilmGenres(_film.getId(), _film.getGenres());

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
        final String sqlStatement = String.format("select * from Genre where %1$s in (select %2$s from FilmGenre where %3$s = :%3$s) order by %1$s"
                , GenreDAO.ID_FIELD, FILMGENRE_GENRE_ID, FILMGENRE_FILM_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(FILMGENRE_FILM_ID, id);
        List<Genre> genres = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> GenreDAO.makeGenre(rs));

        return Film.builder()
                .id(FilmIdType.of(id))
                .name(_rs.getString(NAME_FIELD))
                .description(_rs.getString(DESCRIPRTION_FIELD))
                .releaseDate(_rs.getDate(RELEASE_FIELD).toLocalDate())
                .duration(_rs.getInt(DURATION_FIELD))
                .mpa(RankMPADAO.makeRankMPA(_rs))
                .genres(genres)
                .build();
    }

    public List<Film> getPopular(int _maxCount) {
        final String sqlStatement = String.format(" select top :%1$s film.*, RankMPA.*, Likes.count\n" +
                "from film\n" +
                "join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id\n" +
                "left outer join (select film_id, count(*) as count from FilmLikes group by film_id) as Likes on Likes.film_id=film.film_id\n" +
                "order by Likes.count desc, film.film_id", MAX_COUNT);
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(MAX_COUNT, _maxCount);
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.getPopular({})", this.getClass().getName(), _maxCount);

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

    public List<Film> getSearchedFilms(String _query, Set<String> _by) { //add-search
        if (_by.contains("title")) {
            _by.remove("title");
            _by.add(NAME_FIELD);
        }
        StringBuilder sqlStatementBuilder = new StringBuilder("SELECT f.*, r.RankMPA_name FROM Film AS f " +
                "LEFT JOIN FilmLikes AS fl ON f.film_id = fl.film_id " +
                "LEFT JOIN RankMPA AS r on r.rankMPA_id=f.rankMPA_id " +
                "WHERE ");
        if (areValidNames(_by) && !_query.contains("'") && !_query.contains(";")) {
            for (int i = 0; i < _by.size(); i++) {
                sqlStatementBuilder.append(i == _by.size() - 1 ? "f." + _by.toArray()[i] : "f." + _by.toArray()[i] +
                        " ILIKE '%" + _query + "%' OR ");
            }
            sqlStatementBuilder.append(" ILIKE '%" + _query + "%' " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(fl.film_id) DESC, f.film_name");
            List<Film> result = jdbcNamedTemplate.query(sqlStatementBuilder.toString(), (rs, rowNum) -> makeFilm(rs));
            log.info("Выполнено {}.getSearchedFilms(query: {}, by: {})", this.getClass().getName(), _query, _by);
            return result;
        }
        throw new KeyNotFoundException("Неверные параметры запроса", this.getClass(), log);
    }

    private boolean areValidNames(Set<String> by) {
        for (String s : by) {
            if (!validColumns.contains(s)) {
                return false;
            }
        }
        return true;
    }
}
