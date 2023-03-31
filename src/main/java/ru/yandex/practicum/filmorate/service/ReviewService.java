package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;

@Slf4j
@Service
public class ReviewService extends BaseItemService<ReviewIdType, Review, ReviewStorage> {
    private final EventStorage eventStorage;

    @Override
    public Review createItem(final Review item) {
        Review result = super.createItem(item);
        eventStorage.addEvent(result.getUserId(), result.getReviewId().getValue(), EventType.REVIEW, OperationType.ADD);
        return result;
    }

    @Override
    public Review updateItem(final Review item) {
        Review result = super.updateItem(item);
        eventStorage.addEvent(result.getUserId(), result.getReviewId().getValue(), EventType.REVIEW, OperationType.UPDATE);
        return result;
    }

    @Override
    public void deleteItem(final ReviewIdType id) {
        Review review = this.readItem(id);
        super.deleteItem(id);
        eventStorage.addEvent(review.getUserId(), review.getReviewId().getValue(), EventType.REVIEW, OperationType.REMOVE);
    }

    public ReviewService(ReviewStorage reviewStorage, EventStorage eventStorage) {
        super(reviewStorage);
        this.eventStorage = eventStorage;
    }

    public void addLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), reviewId, userId);
        storage.addLike(reviewId, userId);
    }

    public void deleteLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.deleteLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.deleteLike(reviewId, userId);
    }

    public void addDisLike(ReviewIdType reviewId, UserIdType userId) {
        log.debug("Вызов {}.addDisLike({}, {})", this.getClass().getName(), reviewId,  userId);
        storage.addDisLike(reviewId, userId);
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
