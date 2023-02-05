package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;
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

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable FilmIdType filmId) {
        Film film = service.get(filmId);
        log.info("Получение по ключу {} {}", filmId, film);
        return film;
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable FilmIdType filmId, @PathVariable UserIdType userId) {
        log.info("Добавление лайка для фильма {} от пользователя {}", filmId, userId);
        service.addLike(filmId, userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable FilmIdType filmId, @PathVariable UserIdType userId) {
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