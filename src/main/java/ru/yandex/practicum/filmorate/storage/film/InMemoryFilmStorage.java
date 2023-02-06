package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<FilmIdType, Film> films = new TreeMap<>();

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean notExits(FilmIdType filmId) {
        return !films.containsKey(filmId);
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void updateFilm(Film film) {
        films.get(film.getId()).updateWith(film);
    }

    public Film getFilm(FilmIdType filmId) {
        return films.get(filmId);
    }
}
