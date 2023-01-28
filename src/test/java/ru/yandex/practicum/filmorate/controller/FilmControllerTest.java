package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends CommonControllerTest<Film> {
    @BeforeEach
    void initFilm() {
        item = new Film(0, "Film name", "Description", LocalDate.now(), 90*60);
        controller =  new FilmController();
    }

    @Test
    void shouldRejectName() {
        item.setName("");
        assertEquals(1, validator.validate(item).size());
    }

    @Test
    void shouldRejectDescription() {
        StringBuilder sb = new StringBuilder();
        while (sb.length()<=Film.MAX_DESCRIPTION_LENGTH) {
            sb.append("Another line of description.\n");
        }
        item.setDescription(sb.toString());
        assertEquals(1, validator.validate(item).size());
    }

    @Test
    void shouldRejectReleaseDate() {
        item.setReleaseDate(LocalDate.of(1895,12,28));
        assertEquals(0, validator.validate(item).size());

        item.setReleaseDate(item.getReleaseDate().minusDays(1));
        assertEquals(1, validator.validate(item).size());
    }

    @Test
    void shouldRejectDuration() {
        item.setDuration(1);
        assertEquals(0, validator.validate(item).size());

        item.setDuration(0);
        assertEquals(1, validator.validate(item).size());
    }
}