package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Slf4j
@Service
public class FilmService  {
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final TreeMap<Integer, Set<FilmIdType>> rating = new TreeMap<>();
    private FilmIdType lastFilmId;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.lastFilmId =  new FilmIdType(0L);
    }

    private synchronized FilmIdType getNewId() {
        lastFilmId =  new FilmIdType(lastFilmId.getValue()+1);
        return lastFilmId;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film create(final Film film) {
        FilmIdType key = getNewId();
        film.setId(key);
        filmStorage.addFilm(film);
        addFilmToRating(film.getId(), film.getRating());
        return film;
    }

    public Film update(final Film film) {
        FilmIdType id = film.getId();

        if (filmStorage.notExits(id)) {
            throw new KeyNotFoundException("Обновление: не найден ключ "+id+"! "+ film, this.getClass(), log);
        }
        filmStorage.updateFilm(film);
        log.info("Обновление {}", film);
        return film;
    }

    public Film get(FilmIdType key) {
        if (filmStorage.notExits(key)) {
            throw new KeyNotFoundException("Получение: не найден ключ "+key+"!", this.getClass(), log);
        }
        return filmStorage.getFilm(key);
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        if (filmStorage.notExits(filmId)) {
            throw new KeyNotFoundException("Добавление лайков: не найден фильм "+filmId+"!", this.getClass(), log);
        }
        if (userStorage.notExits(userId)) {
            throw new KeyNotFoundException("Добавление лайков: не найден пользователь "+userId+"!", this.getClass(), log);
        }
        Film film = filmStorage.getFilm(filmId);
        removeFilmFromRating(film.getId(), film.getRating());
        film.addLike(userId);
        addFilmToRating(film.getId(), film.getRating());
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        if (filmStorage.notExits(filmId)) {
            throw new KeyNotFoundException("Удаление лайков: не найден фильм "+filmId+"!", this.getClass(), log);
        }
        if (userStorage.notExits(userId)) {
            throw new KeyNotFoundException("Удаление лайков: не найден пользователь "+userId+"!", this.getClass(), log);
        }
        Film film = filmStorage.getFilm(filmId);
        removeFilmFromRating(film.getId(), film.getRating());
        film.removeLike(userId);
        addFilmToRating(film.getId(), film.getRating());
    }

    private void removeFilmFromRating(FilmIdType filmId, int likeCount) {
        rating.get(-likeCount).remove(filmId);
    }

    private void addFilmToRating(FilmIdType filmId, int likeCount) {
        rating.computeIfAbsent(-likeCount, k -> new TreeSet<>()).add(filmId);
    }
    public List<Film> getPopular(int maxCount) {
        List<Film> result = new LinkedList<>();
        for (Set<FilmIdType> filmSet : rating.values()) {
            for (FilmIdType id: filmSet) {
                result.add(filmStorage.getFilm(id));
                if (result.size()>=maxCount) { break; }
            }
            if (result.size()>=maxCount) { break; }
        }
        return result;
    }
}
