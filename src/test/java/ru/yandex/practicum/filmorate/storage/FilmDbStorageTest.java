package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rankmpa.RankMPAStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final RankMPAStorage mpaStorage;


    @Test
    @Order(2)
    void testFilmStorage() {
        final String dummyNameOne = String.valueOf(LocalDate.now().getYear());
        final String dummyNameTwo = dummyNameOne + "+";
        List<Film> filmList = filmStorage.readAllItems();
        int lastSize = filmList.size();
        Genre genre1 = genreStorage.createItem(Genre.builder().name(dummyNameOne).build());
        RankMPA mpa1 = mpaStorage.createItem(RankMPA.builder().name(dummyNameOne).build());
        Genre genre2 = genreStorage.createItem(Genre.builder().name(dummyNameTwo).build());
        RankMPA mpa2 = mpaStorage.createItem(RankMPA.builder().name(dummyNameTwo).build());
        FilmIdType[] dummyId = {null};
        Film item2Create = Film.builder()
                .name(dummyNameOne)
                .description(dummyNameOne)
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(new ArrayList<>(List.of(genre1)))
                .mpa(mpa1)
                .build();
        final Film[] item = {filmStorage.createItem(item2Create)};
        assertEquals(dummyNameOne, item[0].getName());
        assertEquals(1, item[0].getGenres().size());
        assertEquals(dummyNameOne, item[0].getGenres().get(0).getName());
        assertEquals(dummyNameOne, item[0].getMpa().getName());
        assertEquals(1, filmStorage.readAllItems().size() - lastSize);

        item[0].setName(dummyNameTwo);
        item[0].setMpa(mpa2);
        item[0].getGenres().add(genre2);
        assertDoesNotThrow(() -> item[0] = filmStorage.updateItem(item[0]));
        assertEquals(1, filmStorage.readAllItems().size() - lastSize);
        assertEquals(dummyNameTwo, item[0].getName());
        assertEquals(2, item[0].getGenres().size());
        assertEquals(dummyNameOne, item[0].getGenres().get(0).getName());
        assertEquals(dummyNameTwo, item[0].getGenres().get(1).getName());
        assertEquals(dummyNameTwo, item[0].getMpa().getName());

        dummyId[0] = FilmIdType.of(-999L);

        assertThrows(KeyNotFoundException.class, () -> filmStorage.deleteItem(dummyId[0]));

        item[0].getGenres().clear();
        assertDoesNotThrow(() -> item[0] = filmStorage.updateItem(item[0]));
        assertDoesNotThrow(() -> filmStorage.deleteItem(item[0].getId()));
        assertEquals(lastSize, filmStorage.readAllItems().size());

        assertThrows(KeyNotFoundException.class, () -> filmStorage.deleteItem(dummyId[0]));
    }

    @Test
    @Order(1)
    void testFilmStorageWithLikes() {
        final String nameOne = String.valueOf(LocalDate.now().getYear());
        final String nameTwo = nameOne + "+";

        int lastPopularCount = filmStorage.getPopular(100).size();

        RankMPA mpa = mpaStorage.readAllItems().get(0);
        Film[] film = {Film.builder().name(nameOne).description(nameOne).duration(100).releaseDate(LocalDate.now()).mpa(mpa).build(),
                Film.builder().name(nameTwo).description(nameTwo).duration(200).releaseDate(LocalDate.now()).mpa(mpa).build()};
        User userOne = User.builder().name(nameOne).login(nameOne).email(nameOne).birthday(LocalDate.now().minusYears(1)).build();
        User userTwo = User.builder().name(nameTwo).login(nameTwo).email(nameTwo).birthday(LocalDate.now().minusYears(1)).build();
        userOne = userStorage.createItem(userOne);
        userTwo = userStorage.createItem(userTwo);
        UserIdType fakeUserId = UserIdType.of(-999L);

        film[0] = filmStorage.createItem(film[0]);
        assertEquals(lastPopularCount + 1, filmStorage.getPopular(10).size());
        film[1] = filmStorage.createItem(film[1]);

        FilmIdType[] filmId = {film[0].getId(), film[1].getId()};

        assertEquals(1, filmStorage.getPopular(1).size());
        assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
        filmStorage.addLike(film[1].getId(), userOne.getId());
        assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
        assertEquals(film[1].getId(), filmStorage.getPopular(10).get(0).getId());
        assertEquals(film[0].getId(), filmStorage.getPopular(10).get(1).getId());

        assertThrows(DataIntegrityViolationException.class, () -> filmStorage.addLike(filmId[0], fakeUserId));

        filmStorage.addLike(film[0].getId(), userOne.getId());
        filmStorage.addLike(film[0].getId(), userTwo.getId());
        assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
        assertEquals(filmId[0], filmStorage.getPopular(10).get(0).getId());
        assertEquals(filmId[1], filmStorage.getPopular(10).get(1).getId());

        filmStorage.removeLike(filmId[0], userOne.getId());
        filmStorage.removeLike(filmId[0], userTwo.getId());
        assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
        assertEquals(filmId[1], filmStorage.getPopular(10).get(0).getId());
        assertEquals(filmId[0], filmStorage.getPopular(10).get(1).getId());
    }


}