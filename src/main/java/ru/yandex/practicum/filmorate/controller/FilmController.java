package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<FilmIdType, Film> {
    private final FilmService filmService;

    public FilmController(FilmService _filmService) {
        super(_filmService);
        filmService = _filmService;
    }

    //PUT /films/{id}/like/{userId}  — пользователь ставит лайк фильму.
    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable(name="filmId") FilmIdType _filmId, @PathVariable(name="userId") UserIdType _userId) {
        log.debug("Вызов {}.addLike({}, {})", this.getClass().getName(), _filmId, _userId);
        filmService.addLike(_filmId, _userId);
    }

    //DELETE /films/{id}/like/{userId}  — пользователь удаляет лайк.
    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable(name="filmId") FilmIdType _filmId, @PathVariable(name="userId") UserIdType _userId) {
        log.debug("Вызов {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
        filmService.removeLike(_filmId, _userId);
    }

    //GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(name = "count", defaultValue="10") int _count) {
        log.debug("Вызов {}.getPopular({})", this.getClass().getName(), _count);
        return filmService.getPopular(_count);
    }
}