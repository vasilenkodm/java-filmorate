package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

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
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.removeLike(_filmId, _userId);
    }

    public List<Film> getPopular(int _maxCount) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _maxCount);
        return storage.getPopular(_maxCount);
    }

    public List<Film> getSearchedFilms(String _query, Set<String> _by) { //add-search
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), _query,  _by);
        return storage.getSearchedFilms(_query, _by);
    }
}
