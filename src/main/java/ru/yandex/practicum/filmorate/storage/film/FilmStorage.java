package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.type.FilmIdType;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();
    boolean notExits(FilmIdType filmId);
    void addFilm(Film film);
    void updateFilm(Film film);
    Film getFilm(FilmIdType filmId);
    List<Film> getPopular(int maxCount);

}
