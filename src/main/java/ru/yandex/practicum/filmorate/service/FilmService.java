package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService extends BaseItemService<FilmIdType, Film, FilmStorage> {
    private final EventStorage eventStorage;

    public FilmService(FilmStorage storage, EventStorage eventStorage) {
        super(storage);
        this.eventStorage = eventStorage;
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), filmId,  userId);
        storage.addLike(filmId, userId);
        eventStorage.addEvent(userId, filmId, EventType.LIKE, OperationType.ADD);
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), filmId,  userId);
        storage.removeLike(filmId, userId);
        eventStorage.addEvent(userId, filmId, EventType.LIKE, OperationType.REMOVE);
    }

    public List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year) {
        log.debug("Вызов {}.getPopular({}, {}, {})", this.getClass().getName(), maxCount, genreId, year);
        return storage.getPopular(maxCount, genreId, year);
    }

    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        return storage.getFilmsByDirector(directorId, sortBy);
    }

    public List<Film> getSearchedFilms(String query, Set<String> by) {
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), query,  by);
        return storage.getSearchedFilms(query, by);
    }

    public List<Film> getCommonFilms(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.getCommonFilms({}, {})", this.getClass().getName(), userId, friendId);
        return storage.getCommonFilms(userId, friendId);
    }
}
