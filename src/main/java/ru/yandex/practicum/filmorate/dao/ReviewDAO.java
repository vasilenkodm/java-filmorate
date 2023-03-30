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
import java.util.List;
import java.util.Objects;

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

    public static String idNotFoundMsg(ReviewIdType id) {
        return String.format("Не найден отзыв с кодом %s!", id);
    }

    @Override
    public Review create(Review item) {
        final String sqlStatement = String.format("insert into Review (%1$s, %2$s, %3$s, %4$s) values ( :%1$s, :%2$s, :%3$s, :%4$s )"
                , CONTENT_FIELD, IS_POSITIVE_FIELD, USER_ID_FIELD, FILM_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(CONTENT_FIELD, item.getContent())
                .addValue(IS_POSITIVE_FIELD, item.getIsPositive())
                .addValue(USER_ID_FIELD, item.getUserId())
                .addValue(FILM_ID_FIELD, item.getFilmId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        ReviewIdType newId = ReviewIdType.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
        Review result = (Review) item.clone();
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), item, newId);
        return result;
    }

    @Override
    public void update(Review item) {
        final String sqlStatement = String.format("update Review set %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s, %5$s = :%5$s  where %1$s = :%1$s",
                ID_FIELD, CONTENT_FIELD, IS_POSITIVE_FIELD, USER_ID_FIELD, FILM_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, item.getId().getValue())
                .addValue(CONTENT_FIELD, item.getContent())
                .addValue(IS_POSITIVE_FIELD, item.getIsPositive())
                .addValue(USER_ID_FIELD, item.getUserId())
                .addValue(FILM_ID_FIELD, item.getFilmId());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(item.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), item);
    }

    @Override
    public Review read(ReviewIdType id) {
        String sqlStatement = String.format("select * from review where %1$s = :%1$s", "review_id");
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("review_id", id.getValue());
        Review result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeReview(rs));
        } catch (EmptyResultDataAccessException ex){
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
        final String sqlStatement = String.format("select * from Review order by %1$s", "review_id");
        List<Review> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeReview(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public void addLike(ReviewIdType reviewId, UserIdType userId){final String sqlStatement = String.format("insert into  ReviewLikes (%1$s, %2$s, %3$s) values( :%1$s , :%2$s , :%3$s )", "review_id", "user_id", "is_like");
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("review_id", reviewId.getValue())
                .addValue("user_id", userId.getValue())
                .addValue("is_like", true);
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), reviewId, userId);
    }
    public void removeLike(ReviewIdType reviewId, UserIdType userId){final String sqlStatement = String.format("delete from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, USER_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("review_id", reviewId.getValue())
                .addValue("user_id", userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), reviewId, userId);
    }
    public void addDisLike(ReviewIdType reviewId, UserIdType userId){final String sqlStatement = String.format("insert into  ReviewLikes (%1$s, %2$s, %3$s) values( :%1$s , :%2$s , :%3$s )", ID_FIELD, USER_ID_FIELD, "is_like");
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("review_id", reviewId.getValue())
                .addValue("user_id", userId.getValue())
                .addValue("is_like", false);
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.addDisLike({}, {})", this.getClass().getName(), reviewId, userId);
    }
    public void removeDisLike(ReviewIdType reviewId, UserIdType userId){
        //jdbcTemplate.update("DELETE FROM review_likes where review_id = ? AND user_id = ?", _reviewId, _userId);
        final String sqlStatement = String.format("delete from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, USER_ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("review_id", reviewId.getValue())
                .addValue("user_id", userId.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.removeDisLike({}, {})", this.getClass().getName(), reviewId, userId);
    }

    public List<Review> getReviewsForFilm(FilmIdType filmId, int count) {
        if(filmId != null) {
            return getReviewsByFilmId(filmId, count);
        } else {
            return selectAll();
        }
    }

    private List<Review> getReviewsByFilmId(FilmIdType filmId, int count) {final String sqlStatement = String.format("select top :%2$s * from Review where %1$s = :%1$s ", FILM_ID_FIELD, "count");
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(FILM_ID_FIELD, filmId.getValue())
                .addValue("count", count);
        List<Review> result =  jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeReview(rs));

        log.info("Выполнено {}.getReviewsForFilm({})", this.getClass().getName(), filmId);
        return result;
    }

    private Integer getUsefulRateForReview(ReviewIdType reviewId) {final String sqlStatement = String.format("select count(*) as %3$s from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, "is_like", "count");
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue("is_like", true);
        int countLikes;

        final String sqlStatement2 = String.format("select count(*) as %3$s from ReviewLikes where %1$s = :%1$s and %2$s = :%2$s", ID_FIELD, "is_like", "count");
        SqlParameterSource sqlParams2 = new MapSqlParameterSource()
                .addValue(ID_FIELD, reviewId.getValue())
                .addValue("is_like", false);
        int countDisLikes;
        try {
            countLikes = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> rs.getInt("count")));
            countDisLikes = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement2, sqlParams2, (rs, row) -> rs.getInt("count")));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(reviewId), this.getClass(), log);
        }

        log.info("Выполнено {}.getUsefulRateForReview({})", this.getClass().getName(), reviewId);
        return countLikes - countDisLikes;
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        log.debug("Вызов {}.makeReview({})", ReviewDAO.class.getName(), rs);
        return Review.builder()
                .id(ReviewIdType.of(rs.getInt(ID_FIELD)))
                .content(rs.getString(CONTENT_FIELD))
                .isPositive(rs.getBoolean(IS_POSITIVE_FIELD))
                .userId(rs.getInt(USER_ID_FIELD))
                .filmId(rs.getInt(FILM_ID_FIELD))
                .useful(getUsefulRateForReview(ReviewIdType.of(rs.getInt(ID_FIELD))))
                .build();
    }

    /*private Integer makeCount(ResultSet rs) throws SQLException {
        return rs.getInt("count");
    }*/
}
