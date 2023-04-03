package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
public class FilmDbStorage extends BaseItemDbStorage<FilmIdType, Film, FilmDAO> implements FilmStorage {
    private final DirectorDAO directorDAO;
    private final UserDAO userDAO;

    public FilmDbStorage(FilmDAO dao, DirectorDAO directorDAO, UserDAO userDAO) {
        super(dao);
        this.directorDAO = directorDAO;
        this.userDAO = userDAO;
    }

    @Override
    public List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year) {
        log.debug("Вызов {}.getPopular({}, {}, {})", this.getClass().getName(), maxCount, genreId, year);

        return dao.getPopular(maxCount, genreId, year);
    }

    @Override
    public int getLikesCount(FilmIdType id) {
        log.debug("Вызов {}.getLikesCount({})", this.getClass().getName(), id);
        return dao.getLikesCount(id);
    }

    @Override
    public void addLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
        dao.addLike(filmId, userId);
    }

    @Override
    public void removeLike(FilmIdType filmId, UserIdType userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
        dao.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        if (directorDAO.notExists(directorId)) {
            throw new KeyNotFoundException(DirectorDAO.idNotFoundMsg(directorId), this.getClass(), log);
        }
        return dao.getFilmsByDirector(directorId, sortBy);
    }

    @Override
    public List<Film> getSearchedFilms(String query, Set<String> by) {
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), query,  by);
        return dao.getSearchedFilms(query, by);
    }

    @Override
    public List<Film> getRecommendations(UserIdType userId) {
        log.debug("Вызов {}.getRecommendations({})", this.getClass().getName(), userId);
        return dao.getRecommendations(userId);
    }

    @Override
    public List<Film> getCommonFilms(UserIdType userId, UserIdType friendId) {
        if (userDAO.notExists(userId)) {
            throw new KeyNotFoundException(UserDAO.idNotFoundMsg(userId), this.getClass(), log);
        }
        if (userDAO.notExists(friendId)) {
            throw new KeyNotFoundException(UserDAO.idNotFoundMsg(friendId), this.getClass(), log);
        }
        log.debug("Вызов {}.getCommonFilms({}, {})", this.getClass().getName(), userId, friendId);
        return dao.getCommonFilms(userId, friendId);
    }
}
