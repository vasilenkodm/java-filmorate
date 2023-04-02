package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseItemController<FilmIdType, Film, FilmService> {
    public FilmController(FilmService filmService) {
        super(filmService);
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable FilmIdType filmId, @PathVariable UserIdType userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), filmId, userId);
        service.addLike(filmId, userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable FilmIdType filmId, @PathVariable UserIdType userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
        service.removeLike(filmId, userId);
    }

    //GET /films/popular?count={limit}&genreId={genreId}&year={year}
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count,
                                 @RequestParam(required = false) GenreIdType genreId,
                                 @RequestParam(required = false) Integer year) {
        log.debug("Вызов {}.getPopular({}, {}, {})", this.getClass().getName(), count, genreId, year);
        return service.getPopular(count, genreId, year);
    }

    //GET /films/director/{directorId}?sortBy={sortBy}
    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable DirectorIdType directorId,
                                         @RequestParam(defaultValue = "year") String sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        return service.getFilmsByDirector(directorId, FilmsByDirectorSortByMode.fromString(sortBy));
    }

    //GET /films/common?userId={userId}&friendId={friendId}
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam UserIdType userId, @RequestParam UserIdType friendId) {
        log.debug("Вызов {}.getCommonFilms({}, {})", this.getClass().getName(), userId, friendId);
        return service.getCommonFilms(userId, friendId);
    }
}