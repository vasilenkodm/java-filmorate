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

@Slf4j
@Service
public class FilmService extends BaseItemService<FilmIdType, Film, FilmStorage> {

    public FilmService(FilmStorage _storage) {
        super(_storage);
    }

    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.addLike(_filmId, _userId);
    }

    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
        storage.removeLike(_filmId, _userId);
    }

    public List<Film> getPopular(int _maxCount) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _maxCount);
        return storage.getPopular(_maxCount);
    }

    public List<Film> getFilmsByDirector(DirectorIdType _directorId, FilmsByDirectorSortByMode _sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), _directorId, _sortBy);
        return storage.getFilmsByDirector(_directorId, _sortBy);
    }
}
