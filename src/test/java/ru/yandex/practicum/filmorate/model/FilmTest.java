package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.GenreIdType;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    @Test
    void testCloning() {
        Film origin = Film.builder()
                .id(FilmIdType.of(10L))
                .name("film")
                .mpa(RankMPA.builder()
                        .id(RankMPAIdType.of(1))
                        .name("rank")
                        .build())
                .genres(new ArrayList<>(List.of(Genre.builder()
                        .id(GenreIdType.of(1))
                        .name("genre")
                        .build())))
                .build();
        Film clone = (Film) origin.makeCopy();


        assertNotSame(origin.getId(), clone.getId(), "getId");
        assertNotSame(origin.getName(), clone.getName(), "getName");
        assertNotSame(origin.getMpa(), clone.getMpa(), "getMpa");
        assertNotSame(origin.getGenres(), clone.getGenres(), "getGenres()");
        assertNotSame(origin.getGenres().get(0), clone.getGenres().get(0), "getGenres().get(0)");

        assertEquals(origin.getId(), clone.getId(), "getId");
        assertEquals(origin.getName(), clone.getName(), "getName");
        assertEquals(origin.getMpa(), clone.getMpa(), "getMpa");
        assertEquals(origin.getGenres().get(0), clone.getGenres().get(0), "getGenres().get(0)");

        clone.getGenres().add(Genre.builder()
                .id(GenreIdType.of(2))
                .name("genre+")
                .build());

        assertNotEquals(origin.getGenres().size(), clone.getGenres().size(), "getGenres()");

    }
}