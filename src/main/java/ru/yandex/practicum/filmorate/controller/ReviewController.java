package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //GET /reviews?filmId={filmId}&count={count}
    //Получение всех отзывов по идентификатору фильма, если фильм не указа но все. Если кол-во не указано то 10.

    @GetMapping
    public List<Review> readAllItems(@RequestParam(required = false) FilmIdType filmId,
                                     @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.debug("Вызов {}.readAllItems({})", this.getClass().getName(), filmId);
        return reviewService.readAllItems(filmId, count);
    }

    //PUT /reviews/{id}/like/{userId}  — пользователь ставит лайк отзыву.
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") ReviewIdType reviewId, @PathVariable("userId") UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), reviewId, userId);
        reviewService.addLike(reviewId, userId);
    }

    //PUT /reviews/{id}/dislike/{userId}  — пользователь ставит дизлайк отзыву.
    @PutMapping("/{id}/dislike/{userId}")
    public void addDisLike(@PathVariable("id") ReviewIdType reviewId, @PathVariable("userId")UserIdType userId) {
        log.debug("Вызов {}.addDisLike({}, {})", this.getClass().getName(), reviewId, userId);
        reviewService.addDisLike(reviewId, userId);
    }

    //DELETE /reviews/{id}/like/{userId}  — пользователь удаляет лайк/дизлайк отзыву.
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") ReviewIdType reviewId, @PathVariable("userId")UserIdType userId) {
        log.debug("Вызов {}.deleteLike({}, {})", this.getClass().getName(), reviewId, userId);
        reviewService.deleteLike(reviewId, userId);
    }

    //DELETE /reviews/{id}/dislike/{userId}  — пользователь удаляет дизлайк отзыву.
    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDisLike(@PathVariable("id") ReviewIdType reviewId, @PathVariable("userId")UserIdType userId) {
        log.debug("Вызов {}.deleteDisLike({}, {})", this.getClass().getName(), reviewId, userId);
        reviewService.deleteDisLike(reviewId, userId);
    }

    @GetMapping("/{id}")
    public Review readItem(@PathVariable ReviewIdType id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), id);
        return reviewService.readItem(id);
    }

    @PostMapping
    public Review createItem(@Valid @RequestBody Review item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), item);
        return reviewService.createItem(item);
    }

    @PutMapping
    public Review updateItem(@Valid @RequestBody Review item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), item);
        return reviewService.updateItem(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable ReviewIdType id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), id);
        reviewService.deleteItem(id);
    }

}
