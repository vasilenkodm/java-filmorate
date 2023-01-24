package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends CommonControllerTest<Film> {

    @BeforeEach
    void initFilm() {
        item = new Film(1, "Film name", "Description", LocalDate.of(2000, 12, 31), 90*60);
        controller =  new FilmController();
    }

    @Test
    void shouldRejectName() {
        item.setName("");
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

    @Test
    void shouldRejectDescription() {
        StringBuilder sb = new StringBuilder();
        while (sb.length()<=FilmController.MAX_DESCRIPTION_LENGTH) {
            sb.append("Another line of description.\n");
        }
        item.setDescription(sb.toString());
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

    @Test
    void shouldRejectReleaseDate() {
        item.setReleaseDate(FilmController.MINMAL_RELEASE_DATE);
        assertDoesNotThrow(() -> controller.validate(item));
        item.setReleaseDate(FilmController.MINMAL_RELEASE_DATE.minusDays(1));
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

    @Test
    void shouldRejectDuration() {
        item.setDuration(1);
        assertDoesNotThrow(() -> controller.validate(item));
        item.setDuration(0);
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

}