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

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseItemController<FilmIdType, Film, FilmService> {
    public FilmController(FilmService _filmService) {
        super(_filmService);
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable(name="filmId") FilmIdType _filmId, @PathVariable(name="userId") UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _filmId, _userId);
        service.addLike(_filmId, _userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable(name="filmId") FilmIdType _filmId, @PathVariable(name="userId") UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
        service.removeLike(_filmId, _userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue = "10") int _count) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _count);
        return service.getPopular(_count);
    }

    //GET /films/director/{directorId}?sortBy={sortBy}
    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable(name = "directorId") DirectorIdType _directorId
            , @RequestParam(name = "sortBy", defaultValue = "year") String _sortBy) {
        log.debug("Вызов {}.getFilmsByDirector({}, {})", this.getClass().getName(), _directorId, _sortBy);
        return service.getFilmsByDirector(_directorId, FilmsByDirectorSortByMode.fromString(_sortBy));
    }


}