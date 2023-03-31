package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.DirectorIdType;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.FilmsByDirectorSortByMode;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

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

    public List<Film> getPopular(int maxCount) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), maxCount);
        return storage.getPopular(maxCount);
    }

    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        return storage.getFilmsByDirector(directorId, sortBy);
    }

    public List<Film> getSearchedFilms(String query, Set<String> by) {
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), query,  by);
        return storage.getSearchedFilms(query, by);
    }
}
