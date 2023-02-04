package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService  {
    final private FilmStorage filmStorage;
    final private UserStorage userStorage;
    private FilmIdType lastFilmId;

    @Autowired
    FilmService(FilmStorage filmStorage, UserStorage userStorage) {
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
        return film;
    }

    public Film update(final Film film) {
        FilmIdType key = film.getId();

        if (!filmStorage.exits(key)) {
            throw new KeyNotFoundException("Обновление: не найден ключ "+key+"! "+film.toString(), this.getClass(), log);
        }

        filmStorage.updateFilm(film);

        log.info("Обновление {}", film);
        return film;
    }

    public Film get(FilmIdType key) {
        if (!filmStorage.exits(key)) {
            throw new KeyNotFoundException("Получение: не найден ключ "+key+"!", this.getClass(), log);
        }
        return filmStorage.getFilm(key);
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        if (!filmStorage.exits(filmId)) {
            throw new KeyNotFoundException("Добавление лайков: не найден фильм "+filmId+"!", this.getClass(), log);
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new KeyNotFoundException("Добавление лайков: не найден пользователь "+userId+"!", this.getClass(), log);
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        if (!filmStorage.exits(filmId)) {
            throw new KeyNotFoundException("Удаление лайков: не найден фильм "+filmId+"!", this.getClass(), log);
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new KeyNotFoundException("Удаление лайков: не найден пользователь "+userId+"!", this.getClass(), log);
        }
        filmStorage.removeLike(filmId, userId);
    }
    public Set<Film> getPopular(int maxCount) {
        return filmStorage.getPopular(maxCount);
    }

}
