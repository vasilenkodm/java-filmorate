package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.type.DirectorIdType;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.FilmsByDirectorSortByMode;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

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

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), count);
        return service.getPopular(count);
    }

    //GET /films/director/{directorId}?sortBy={sortBy}
    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable DirectorIdType directorId,
                                         @RequestParam(defaultValue = "year") String sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), directorId, sortBy);
        return service.getFilmsByDirector(directorId, FilmsByDirectorSortByMode.fromString(sortBy));
    }

    //GET /films/search?query=крад&by=director,title
    @GetMapping("/search") //add-search
    public List<Film> getSearchedFilms(@RequestParam(name = "query") String query,
                                       @RequestParam(name = "by", defaultValue = "title,director") Set<String> by) {
        log.debug("Вызов {}.getSearchedFilms({}, {})", this.getClass().getName(), query,  by);
        return service.getSearchedFilms(query, by);
    }
}