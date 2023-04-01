package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class ReviewService extends BaseItemService<ReviewIdType, Review, ReviewStorage> {
    private final EventStorage eventStorage;

    public ReviewService(ReviewStorage reviewStorage, EventStorage eventStorage) {
        super(reviewStorage);
        this.eventStorage = eventStorage;
    }

    public void addLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.addLike(reviewId, userId);
        eventStorage.addEvent(userId.getValue(), reviewId.getValue(), EventType.LIKE, OperationType.ADD);
    }

    public void deleteLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.deleteLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.deleteLike(reviewId, userId);
        eventStorage.addEvent(userId.getValue(), reviewId.getValue(), EventType.LIKE , OperationType.REMOVE);
    }

    public void addDisLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.addDisLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.addDisLike(reviewId, userId);
        //eventStorage.addEvent(userId.getValue(), reviewId.getValue(), EventType.REVIEW , OperationType.REMOVE);
    }

    public void deleteDisLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.deleteDisLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.deleteDisLike(reviewId, userId);
    }

    public List<Review> readAllItems(FilmIdType filmId, int count) {
        log.debug("Вызов {}.getReviewsForFilm({})", this.getClass().getName(), filmId);
        return storage.readAllItems(filmId, count);
    }
}
