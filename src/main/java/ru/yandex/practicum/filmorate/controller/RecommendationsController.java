package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.RecommendationsService;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class RecommendationsController extends BaseItemController<FilmIdType, Film, RecommendationsService> {
    public RecommendationsController(RecommendationsService service) {
        super(service);
    }

    //GET /users/{userId}/recommendations — список рекомендаций фильмов для пользователя.
    @SuppressWarnings("unused")
    @GetMapping("/users/{userId}/recommendations")
    public List<Film> getRecommendations(@PathVariable UserIdType userId) {
        log.debug("Вызов {}.getRecommendations({})", this.getClass().getName(), userId);
        return service.getRecommendations(userId);
    }
}
