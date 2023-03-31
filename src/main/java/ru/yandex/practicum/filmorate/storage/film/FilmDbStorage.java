package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
public class FilmDbStorage extends BaseItemDbStorage<FilmIdType, Film, FilmDAO> implements FilmStorage {
    public FilmDbStorage(FilmDAO _dao) {
        super(_dao);
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
    public List<Film> getSearchedFilms(String _query, Set<String> _by) { //add-search
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), _query,  _by);
        return dao.getSearchedFilms(_query, _by);
    }
}
