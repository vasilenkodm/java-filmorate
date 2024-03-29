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
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewDAO implements ItemDAO<ReviewIdType, Review> {

    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public static final String ID_FIELD = "review_id";
    public static final String CONTENT_FIELD = "content";
    public static final String IS_POSITIVE_FIELD = "is_positive";
    public static final String USER_ID_FIELD = "user_id";
    public static final String FILM_ID_FIELD = "film_id";
    public static final String IS_LIKE_FIELD = "is_like";
    public static final String COUNT_FIELD = "count";

    public static String idNotFoundMsg(ReviewIdType id) {
        return String.format("Не найден отзыв с кодом %s!", id);
    }

    public static String filmIdNotFoundMsg(FilmIdType id) {
        return String.format("Не найден фильм с кодом %s!", id);
    }

    public static String userIdNotFoundMsg(UserIdType id) {
        return String.format("Не найден пользователь с кодом %s!", id);
    }

    @Override
    public Review create(Review item) {
        final String sqlStatement = String.format("insert into Review (%1$s, %2$s, %3$s, %4$s) values ( :%1$s, :%2$s, :%3$s, :%4$s )",
                CONTENT_FIELD, IS_POSITIVE_FIELD, USER_ID_FIELD, FILM_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(CONTENT_FIELD, item.getContent())
                .addValue(IS_POSITIVE_FIELD, item.getIsPositive())
                .addValue(USER_ID_FIELD, item.getUserId().getValue())
                .addValue(FILM_ID_FIELD, item.getFilmId().getValue());
        isFilmExists(item.getFilmId());
        isUserExists(item.getUserId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        ReviewIdType newId = ReviewIdType.of(Objects.requireNonNull(keyHolder.getKey()).longValue());
        Review result = item.clone();
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), item, newId);
        return result;
    }

    @Override
    public void update(Review item) {
        final String sqlStatement = String.format("update Review set %2$s = :%2$s, %3$s = :%3$s  where %1$s = :%1$s",
                ID_FIELD, CONTENT_FIELD, IS_POSITIVE_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, item.getId().getValue())
                .addValue(CONTENT_FIELD, item.getContent())
                .addValue(IS_POSITIVE_FIELD, item.getIsPositive());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(item.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), item);
    }

    @Override
    public Review read(ReviewIdType id) {
        String sqlStatement = String.format("select * from review where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        Review result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeReview(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), id);

        return result;
    }

    @Override
    public void delete(ReviewIdType id) {
        final String sqlStatement = String.format("delete from Review where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), id);
    }

    @Override
    public List<Review> selectAll() {
        final String sqlStatement = String.format("select * from Review order by %1$s", ID_FIELD);
        List<Review> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeReview(rs))
                .stream().sorted(Comparator.comparing(Review::getUseful).reversed()).collect(Collectors.toList());

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public void addLike(ReviewIdType reviewId, UserIdType userId) {
        final String sqlStatement = String.format("insert into  ReviewLikes (%1$s, %2$s, %3$s) values( :%1$s , :%2$s , :%3$s )", ID_FIELD, USER_ID_FIELD, IS_LIKE_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(USER_ID_FIELD, userId.getValue())
                .addValue(IS_LIKE_FIELD, true);
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }
        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), reviewId, userId);
    }

    public void removeLike(ReviewIdType reviewId, UserIdType userId) {
        final String sqlStatement = String.format("delete from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, USER_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(USER_ID_FIELD, userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);
        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }
        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), reviewId, userId);
    }

    public void addDisLike(ReviewIdType reviewId, UserIdType userId) {
        final String sqlStatement = String.format("insert into  ReviewLikes (%1$s, %2$s, %3$s) values( :%1$s , :%2$s , :%3$s )", ID_FIELD, USER_ID_FIELD, IS_LIKE_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(USER_ID_FIELD, userId.getValue())
                .addValue(IS_LIKE_FIELD, false);
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.addDisLike({}, {})", this.getClass().getName(), reviewId, userId);
    }

    public void removeDisLike(ReviewIdType reviewId, UserIdType userId) {
        final String sqlStatement = String.format("delete from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, USER_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(USER_ID_FIELD, userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);
        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }
        log.info("Выполнено {}.removeDisLike({}, {})", this.getClass().getName(), reviewId, userId);
    }

    public List<Review> readAllItems(FilmIdType filmId, int count) {
        if (filmId != null) {
            return getReviewsByFilmId(filmId, count);
        } else {
            return selectAll();
        }
    }

    private List<Review> getReviewsByFilmId(FilmIdType filmId, int count) {
        final String sqlStatement = String.format("select top :%2$s * from Review where %1$s = :%1$s ", FILM_ID_FIELD, COUNT_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILM_ID_FIELD, filmId.getValue())
                .addValue(COUNT_FIELD, count);
        List<Review> result =  jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeReview(rs))
                .stream().sorted(Comparator.comparing(Review::getUseful).reversed()).collect(Collectors.toList());

        log.info("Выполнено {}.getReviewsForFilm({})", this.getClass().getName(), filmId);
        return result;
    }

    private Integer getUsefulRateForReview(ReviewIdType reviewId) {
        final String sqlStatement = String.format("select count(*) as %3$s from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, IS_LIKE_FIELD, COUNT_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(IS_LIKE_FIELD, true);
        int countLikes;

        final String sqlStatement2 = String.format("select count(*) as %3$s from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, IS_LIKE_FIELD, COUNT_FIELD);
        SqlParameterSource sqlParams2 = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue(IS_LIKE_FIELD, false);
        int countDisLikes;
        try {
            countLikes = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> rs.getInt(COUNT_FIELD)));
            countDisLikes = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement2, sqlParams2, (rs, row) -> rs.getInt(COUNT_FIELD)));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.getUsefulRateForReview({})", this.getClass().getName(), reviewId);
        return countLikes - countDisLikes;
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        log.debug("Вызов {}.makeReview({})", ReviewDAO.class.getName(), rs);
        return Review.builder()
                .reviewId(ReviewIdType.of(rs.getLong(ID_FIELD)))
                .content(rs.getString(CONTENT_FIELD))
                .isPositive(rs.getBoolean(IS_POSITIVE_FIELD))
                .userId(UserIdType.of(rs.getLong(USER_ID_FIELD)))
                .filmId(FilmIdType.of(rs.getLong(FILM_ID_FIELD)))
                .useful(getUsefulRateForReview(ReviewIdType.of(rs.getLong(ID_FIELD))))
                .build();
    }

    private void isFilmExists(FilmIdType id) {
        if (id.getValue() < 0) {
            throw new KeyNotFoundException(filmIdNotFoundMsg(id), this.getClass(), log);
        }
    }

    private void isUserExists(UserIdType id) {
        if (id.getValue() < 0) {
            throw new KeyNotFoundException(userIdNotFoundMsg(id), this.getClass(), log);
        }
    }
}
