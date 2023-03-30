package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDAO;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Component
@Primary
public class ReviewDbStorage extends BaseItemDbStorage<ReviewIdType, Review, ReviewDAO> implements ReviewStorage {
    public ReviewDbStorage(ReviewDAO dao) {
        super(dao);
    }

    @Override
    public void addLike(ReviewIdType _reviewId, UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _reviewId, _userId);
        dao.addLike(_reviewId, _userId);
    }

    @Override
    public void deleteLike(ReviewIdType _reviewId, UserIdType _userId) {
        log.debug("Вызов {}.deleteLike({}, {})", this.getClass().getName(), _reviewId, _userId);
        dao.removeLike(_reviewId, _userId);
    }

    @Override
    public void addDisLike(ReviewIdType _reviewId, UserIdType _userId) {
        log.debug("Вызов {}.addDisLike({}, {})", this.getClass().getName(), _reviewId, _userId);
        dao.addDisLike(_reviewId, _userId);
    }

    @Override
    public void deleteDisLike(ReviewIdType _reviewId, UserIdType _userId) {
        log.debug("Вызов {}.deleteDisLike({}, {})", this.getClass().getName(), _reviewId, _userId);
        dao.removeDisLike(_reviewId, _userId);
    }

    @Override
    public List<Review> getReviewsForFilm(FilmIdType filmId, int count) {
        log.debug("Вызов {}.getReviewsForFilm({})", this.getClass().getName(), filmId);
        return dao.getReviewsForFilm(filmId, count);
    }
}
