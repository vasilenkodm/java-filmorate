package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

public interface ReviewStorage extends ItemStorage<ReviewIdType, Review> {
    void addLike(ReviewIdType _reviewId, UserIdType _userId);
    void deleteLike(ReviewIdType _reviewId, UserIdType _userId);
    void addDisLike(ReviewIdType _reviewId, UserIdType _userId);
    void deleteDisLike(ReviewIdType _reviewId, UserIdType _userId);

    List<Review> getReviewsForFilm(FilmIdType filmId, int count);
}
