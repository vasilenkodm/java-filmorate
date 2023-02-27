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
public class FilmService  extends DefaultService<FilmIdType, Film, FilmStorage> {

    private FilmIdType lastFilmId;

    FilmService(FilmStorage _storage) {
        super(_storage);
        this.lastFilmId =  new FilmIdType(0L);
    }
        

    private synchronized FilmIdType getNewId() {
        log.debug("Вызов {}.getNewId()", this.getClass().getName());
        lastFilmId =  new FilmIdType(lastFilmId.getValue()+1);
        return lastFilmId;
    }

    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.addLike(_filmId, _userId);
    }

    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.removeLike(_filmId, _userId);
    }

    public List<Film> getPopular(int maxCount) {
        return storage.getPopular(maxCount);
    }
}
