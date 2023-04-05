package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class RecommendationsService extends BaseItemService<FilmIdType, Film, FilmStorage> {

    public RecommendationsService(FilmStorage storage) {
        super(storage);
    }

    public List<Film> getRecommendations(UserIdType userId) {
        log.debug("Вызов {}.getRecommendations({})", this.getClass().getName(), userId);
        return storage.getRecommendations(userId);
    }
}
