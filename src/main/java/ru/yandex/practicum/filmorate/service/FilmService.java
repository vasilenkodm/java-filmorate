package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;

@Slf4j
@Service
public class FilmService extends BaseItemService<FilmIdType, Film, FilmStorage> {

    public FilmService(FilmStorage filmStorage) {
        super(filmStorage);
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), filmId, userId);
        storage.addLike(filmId, userId);
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
        storage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year) {
        log.debug("Вызов {}.getPopular({}, {}, {})", this.getClass().getName(), maxCount, genreId, year);
        return storage.getPopular(maxCount, genreId, year);
    }

    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        return storage.getFilmsByDirector(directorId, sortBy);
    }

    public List<Film> getCommonFilms(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.getCommonFilms({}, {})", this.getClass().getName(), userId, friendId);
        return storage.getCommonFilms(userId, friendId);
    }
}
