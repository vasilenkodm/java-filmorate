package ru.yandex.practicum.filmorate.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
public class FilmController extends CommonController<Film> {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final LocalDate MINMAL_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public FilmController() {
        log = LoggerFactory.getLogger(FilmController.class);
    }

    public void validate(Film item) throws ValidationException {
        if (item.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }

        if (item.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!");
        }

        if (item.getReleaseDate().compareTo(MINMAL_RELEASE_DATE) < 0) {
            throw new ValidationException("Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!");
        }

        if (item.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }
    }

}