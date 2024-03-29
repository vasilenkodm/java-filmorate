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
    public static final String DIRECTOR = "director";
    public static final String TITLE = "title";
    public static final String QUERY = "query";

    public static final String MAX_COUNT = "max_count";
    public static final String FRIEND_ID = "friend_id";
    public static final String LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID = "left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id ";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public static String idNotFoundMsg(FilmIdType id) {
        return String.format("Не найден фильм с кодом %s!", id);
    }

    public Film read(final FilmIdType id) {
        String sqlStatement = String.format("select * from Film " +
                LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                "where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        Film result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), id);

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

    public Film create(Film item) {
        final String sqlStatement = String.format("insert into Film (%1$s, %2$s, %3$s, %4$s, %5$s) "
                        + "values ( :%1$s, :%2$s, :%3$s, :%4$s, :%5$s )",
                NAME_FIELD, DESCRIPTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, item.getName())
                .addValue(DESCRIPTION_FIELD, item.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(item.getReleaseDate()))
                .addValue(DURATION_FIELD, item.getDuration())
                .addValue(RANKMPA_FIELD, item.getMpa().getId().getValue());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        FilmIdType newId = FilmIdType.of(Objects.requireNonNull(keyHolder.getKey()).longValue());
        updateFilmGenres(newId, item.getGenres());
        updateFilmDirectors(newId, item.getDirectors());
        Film result = (Film) item.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), item, newId);
        return result;
    }

    public void update(Film item) {
        final String sqlStatement4Film = String.format("update Film set %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s, %5$s = :%5$s, %6$s = :%6$s where %1$s = :%1$s",
                ID_FIELD, NAME_FIELD, DESCRIPTION_FIELD, RELEASE_FIELD, DURATION_FIELD, RANKMPA_FIELD);

        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, item.getId().getValue())
                .addValue(NAME_FIELD, item.getName())
                .addValue(DESCRIPTION_FIELD, item.getDescription())
                .addValue(RELEASE_FIELD, Date.valueOf(item.getReleaseDate()))
                .addValue(DURATION_FIELD, item.getDuration())
                .addValue(RANKMPA_FIELD, item.getMpa().getId().getValue());

        int rowCount = jdbcNamedTemplate.update(sqlStatement4Film, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(item.getId()), this.getClass(), log);
        }
        updateFilmGenres(item.getId(), item.getGenres());
        updateFilmDirectors(item.getId(), item.getDirectors());

        log.info("Выполнено {}.update({})", this.getClass().getName(), item);
    }

    private void updateFilmGenres(FilmIdType filmId, List<Genre> genres) {
        if (genres == null) return;
        log.debug("Вызов {}.updateFilmGenres({}, {})", this.getClass().getName(), filmId, genres);

        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format(" delete from FilmGenre where %1$s = :%1$s; ", FILMGENRE_FILM_ID));
        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMGENRE_FILM_ID, filmId.getValue());
        if (!genres.isEmpty()) {
            sbSQL.append(String.format(" insert into FilmGenre (%1$s, %2$s) values ", FILMGENRE_FILM_ID, FILMGENRE_GENRE_ID));
            boolean needComma = false;
            Set<GenreIdType> uniqueValues = new HashSet<>();
            for (Genre genre : genres) {
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

    private void updateFilmDirectors(FilmIdType filmId, List<Director> directors) {
        log.debug("Вызов {}.updateFilmDirectors({}, {})", this.getClass().getName(), filmId, directors);

        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append(String.format(" delete from FilmDirector where %1$s = :%1$s; ", FILMDIRECTOR_FILM_ID));

        MapSqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMDIRECTOR_FILM_ID, filmId.getValue());
        if (directors != null && !directors.isEmpty()) {
            sbSQL.append(String.format(" insert into FilmDirector (%1$s, %2$s) values ", FILMDIRECTOR_FILM_ID, FILMDIRECTOR_DIRECTOR_ID));
            boolean needComma = false;
            Set<DirectorIdType> uniqueValues = new HashSet<>();
            for (Director director : directors) {
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

    public void delete(FilmIdType id) {
        final String sqlStatement = String.format("delete from Film where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        log.debug("Вызов {}.makeFilm({})", this.getClass().getName(), rs);
        final long id = rs.getLong(ID_FIELD);

        final String sqlStatementGenre = String.format("select G.* from FilmGenre as FG " +
                        "join Genre as G on G.genre_id=FG.genre_id " +
                        "where FG.%1$s = :%1$s " +
                        "order by G.genre_id",
                FILMGENRE_FILM_ID);
        SqlParameterSource sqlParamsGenre = new MapSqlParameterSource().addValue(FILMGENRE_FILM_ID, id);
        List<Genre> genres = jdbcNamedTemplate.query(sqlStatementGenre, sqlParamsGenre, (subRs, row) -> GenreDAO.makeGenre(subRs));

        final String sqlStatementDirector = String.format("select D.* from  FilmDirector as FD " +
                        "join Director as D on FD.director_id=D.director_id " +
                        "where FD.%1$s = :%1$s " +
                        "order by D.director_id",
                FILMDIRECTOR_FILM_ID);
        SqlParameterSource sqlParamsDirector = new MapSqlParameterSource().addValue(FILMDIRECTOR_FILM_ID, id);
        List<Director> directors = jdbcNamedTemplate.query(sqlStatementDirector, sqlParamsDirector, (subRs, row) -> DirectorDAO.makeDirector(subRs));

        return Film.builder()
                .id(FilmIdType.of(id))
                .name(rs.getString(NAME_FIELD))
                .description(rs.getString(DESCRIPTION_FIELD))
                .releaseDate(rs.getDate(RELEASE_FIELD).toLocalDate())
                .duration(rs.getInt(DURATION_FIELD))
                .mpa(RankMPADAO.makeRankMPA(rs))
                .genres(genres)
                .directors(directors)
                .build();
    }

    public List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year) {
        final String sqlStatement = String.format(" select top :%1$s film.*, RankMPA.*, Likes.count " +
                        "from film " +
                        LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                        " %2$s " +
                        "left outer join (select film_id, count(*) as count from FilmLikes group by film_id) as Likes on Likes.film_id=film.film_id " +
                        " %3$s " +
                        "order by Likes.count desc, film.film_id ",
                MAX_COUNT,
                (genreId == null) ? "" : String.format("join FilmGenre on FilmGenre.film_id = film.film_id and FilmGenre.genre_id = :%1$s ",
                        FILMGENRE_GENRE_ID),
                (year == null) ? "" : String.format("where  extract(year from film.release_date) = :%1$s ",
                        RELEASE_FIELD)
        );
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(MAX_COUNT, maxCount)
                .addValue(FILMGENRE_GENRE_ID, (genreId == null) ? null : genreId.getValue())
                .addValue(RELEASE_FIELD, year);
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.getPopular({}, {}, {})", this.getClass().getName(), maxCount, genreId, year);

        return result;
    }

    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        List<Film> result;
        switch (sortBy) {
            case YEAR:
                String sqlStatementYear = String.format(" select film.*, RankMPA.* " +
                        " from film " +
                        " join filmDirector on filmDirector.film_id = film.film_id " +
                        " left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id " +
                        " where filmDirector.%1$s = :%1$s " +
                        " order by  film.release_date, film.film_id", FILMDIRECTOR_DIRECTOR_ID);
                SqlParameterSource sqlParamsYear = new MapSqlParameterSource().addValue(FILMDIRECTOR_DIRECTOR_ID, directorId.getValue());
                result = jdbcNamedTemplate.query(sqlStatementYear, sqlParamsYear, (rs, row) -> makeFilm(rs));
                break;
            case LIKES:
                String sqlStatementLikes = String.format(" select film.*, RankMPA.*, Likes.count " +
                        " from film " +
                        " left outer join RankMPA on RankMPA.rankMPA_id=Film.rankMPA_id " +
                        " left outer join (select film_id, count(*) as count from FilmLikes group by film_id) as Likes on Likes.film_id=film.film_id " +
                        " join filmDirector on filmDirector.film_id = film.film_id " +
                        " where filmDirector.%1$s = :%1$s " +
                        " order by Likes.count, film.film_id", FILMDIRECTOR_DIRECTOR_ID);
                SqlParameterSource sqlParamsLikes = new MapSqlParameterSource().addValue(FILMDIRECTOR_DIRECTOR_ID, directorId.getValue());
                result = jdbcNamedTemplate.query(sqlStatementLikes, sqlParamsLikes, (rs, row) -> makeFilm(rs));
                break;
            default:
                throw new FeatureNotSupportedException(this.getClass(), log);
        }
        if (result.isEmpty())
            throw new KeyNotFoundException(DirectorDAO.idNotFoundMsg(directorId), this.getClass(), log);
        return result;
    }

    public int getLikesCount(FilmIdType id) {
        final String sqlStatement = String.format("select count(*) as %2$s from FilmLikes join Film on Film.film_id = FilmLikes.film_id where %1$s = :%1$s", ID_FIELD, MAX_COUNT);
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(ID_FIELD, id);
        int result;
        try {
            result = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> rs.getInt(MAX_COUNT)));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }
        log.info("Выполнено {}.getLikesCount({})", this.getClass().getName(), id);
        return result;
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_FILM_ID, filmId.getValue())
                .addValue(FILMLIKES_USER_ID, userId.getValue());

        final String sqlStatementClenup = String.format("delete from FilmLikes where %1$s = :%1$s and %2$s= :%2$s", FILMLIKES_FILM_ID, FILMLIKES_USER_ID);
        jdbcNamedTemplate.update(sqlStatementClenup, sqlParams);

        final String sqlStatement = String.format("insert into  FilmLikes (%1$s, %2$s) values( :%1$s , :%2$s )", FILMLIKES_FILM_ID, FILMLIKES_USER_ID);
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(filmId), this.getClass(), log);
        }

        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), filmId, userId);
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        final String sqlStatement = String.format("delete from FilmLikes where %1$s = :%1$s and %2$s = :%2$s", FILMLIKES_FILM_ID, FILMLIKES_USER_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_FILM_ID, filmId.getValue())
                .addValue(FILMLIKES_USER_ID, userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(filmId), this.getClass(), log);
        }

        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
    }

    public List<Film> getRecommendations(UserIdType userId) {
        final String sqlStatement = String.format("select FILM.*, RankMPA.* from film " +
                        LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                        "join FilmLikes FL1 on FILM.film_id = FL1.film_id " +
                        "left outer join FilmLikes FL2 on FILM.film_id = FL2.film_id and FL2.%1$s = :%1$s " +
                        "join (select FLC.user_id, count(FLC.film_id) as count " +
                        "from FilmLikes MyFL " +
                        "join  FilmLikes FLC  on FLC.film_id = MyFL.film_id and FLC.user_id<>MyFL.user_id " +
                        "where MyFL.%1$s = :%1$s " +
                        "group by FLC.user_id  " +
                        "order by  count(FLC.film_id) desc limit 1) as TopFL on  FL1.user_id = TopFL.user_id " +
                        "where FL2.user_id is null ",
                FILMLIKES_USER_ID
        );
        log.debug(sqlStatement);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_USER_ID, userId.getValue());
        List<Film> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));
        log.info("Выполнено {}.getRecommendations({})", this.getClass().getName(), userId);
        return result;
    }

    public List<Film> getSearchedFilms(String query, Set<String> by) {
        if (by.isEmpty()) {
            throw new KeyNotFoundException("Неверные параметры запроса", this.getClass(), log);
        }

        StringBuilder sqlBuilder = new StringBuilder("SELECT Film.*, RankMPA.*, Likes.likes_count FROM Film " +
                "LEFT JOIN (select film_id, count(*) as likes_count from FilmLikes GROUP BY film_id) " +
                "AS Likes on Likes.film_id=Film.film_id " +
                LEFT_OUTER_JOIN_RANK_MPA_ON_RANK_MPA_RANK_MPA_ID_FILM_RANK_MPA_ID +
                "WHERE ");
        boolean needOR = false;
            for (String s : by) {
                if (needOR) {
                    sqlBuilder.append(" OR ");
                }
                switch (s) {
                    case DIRECTOR:
                        sqlBuilder.append("Film.film_id in (SELECT FILM_ID FROM FILMDIRECTOR FD join DIRECTOR D on FD.director_id = D.director_id ")
                                .append(String.format("WHERE D.director_name ILIKE '%%' || :%1$s || '%%') ", QUERY));
                        break;
                    case TITLE:
                        sqlBuilder.append(String.format("film_name ILIKE '%%' || :%1$s || '%%' ", QUERY));
                        break;
                    default:
                        throw new KeyNotFoundException("Неверные параметры запроса", this.getClass(), log);
                }
                needOR = true;
            }

        sqlBuilder.append("ORDER BY Likes.likes_count DESC, Film.film_name");
        SqlParameterSource sqlParams = new MapSqlParameterSource().addValue(QUERY, query);
        List<Film> result = jdbcNamedTemplate.query(sqlBuilder.toString(), sqlParams, (rs, rowNum) -> makeFilm(rs));
        log.info("Выполнено {}.getSearchedFilms(query: {}, by: {})", this.getClass().getName(), query, by);

        return result;
    }

    public List<Film> getCommonFilms(UserIdType userId, UserIdType friendId) {

        final String sqlStatement = String.format("select f.*, r.* " +
                "from film f left join RankMPA r using(RankMPA_id) " +
                "join FilmLikes fl1 on fl1.film_id=film.film_id and fl1.%1$s = :%1$s " +
                "join FilmLikes fl2 on fl2.film_id=film.film_id and fl2.%1$s = :%2$s " +
                "order by f.film_id ASC", FILMLIKES_USER_ID, FRIEND_ID);

        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILMLIKES_USER_ID, userId.getValue())
                .addValue(FRIEND_ID, friendId.getValue());

        List<Film> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeFilm(rs));

        log.info("Выполнено {}.getCommonFilms({}, {})", this.getClass().getName(), userId, friendId);

        return result;
    }
}
