package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getFilms();

    boolean notExits(FilmIdType filmId);
    void addFilm(Film film);
    void updateFilm(Film film);
    Film getFilm(FilmIdType filmId);
    void addLike(FilmIdType filmId, UserIdType userId);
    void removeLike(FilmIdType filmId, UserIdType userId);
    Set<Film> getPopular(int maxCount);
}
