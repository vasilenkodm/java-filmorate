package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.type.*;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService service;
    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Добавление {}", film);
        return service.create(film);
    }
    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Обновление {}", film);
        return service.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable FilmIdType id) {
        Film film = service.get(id);
        log.info("Получение по ключу {} {}", id, film);
        return film;
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{id}/like/{userId}")
    public void addLike(FilmIdType filmId, UserIdType userId) {
        log.info("Добавление лайка для фильма {} от пользователя {}", filmId, userId);
        service.addLike(filmId, userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(FilmIdType filmId, UserIdType userId) {
        log.info("Удаление лайка для фильма {} от пользователя {}", filmId, userId);
        service.removeLike(filmId, userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public Set<Film> getPopular(@RequestParam(defaultValue="10") int count) {
        log.info("Получение {} полуярных фильмов", count);
        return service.getPopular(count);
    }
}