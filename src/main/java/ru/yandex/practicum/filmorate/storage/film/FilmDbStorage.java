package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.DirectorIdType;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.FilmsByDirectorSortByMode;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Component
@Primary
public class FilmDbStorage extends BaseItemDbStorage<FilmIdType, Film, FilmDAO> implements FilmStorage {
    private final DirectorDAO directorDAO;

    public FilmDbStorage(FilmDAO _dao, DirectorDAO _directorDAO) {
        super(_dao);
        directorDAO = _directorDAO;
    }

    @Override
    public List<Film> getPopular(int _maxCount) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _maxCount);

        return dao.getPopular(_maxCount);
    }

    @Override
    public int getLikesCount(FilmIdType _id) {
        log.debug("Вызов {}.getLikesCount({})", this.getClass().getName(), _id);
        return dao.getLikesCount(_id);
    }

    @Override
    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
        dao.addLike(_filmId, _userId);
    }

    @Override
    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
        dao.removeLike(_filmId, _userId);
    }

    @Override
    public List<Film> getFilmsByDirector(DirectorIdType _directorId, FilmsByDirectorSortByMode _sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), _directorId, _sortBy);
        if (directorDAO.notExists(_directorId)) {
            throw new KeyNotFoundException(DirectorDAO.idNotFoundMsg(_directorId), this.getClass(), log);
        }
        return dao.getFilmsByDirector(_directorId, _sortBy);
    }
}
