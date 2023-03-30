package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class FilmService extends BaseItemService<FilmIdType, Film, FilmStorage> {
    final EventStorage eventStorage;;

    public FilmService(FilmStorage _storage, EventStorage eventStorage) {
        super(_storage);
        this.eventStorage = eventStorage;
    }

    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.addLike(_filmId, _userId);
        eventStorage.addEvent(_userId.getValue(), _filmId.getValue(), EventType.LIKE, OperationType.ADD);
    }

    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId,  _userId);
        storage.removeLike(_filmId, _userId);
        eventStorage.addEvent(_userId.getValue(), _filmId.getValue(), EventType.LIKE, OperationType.REMOVE);
    }

    public List<Film> getPopular(int _maxCount) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _maxCount);
        return storage.getPopular(_maxCount);
    }
}
