package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.type.GenreIdType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    @Order(1)
    void testGenreStorage() {
        final String dummyNameOne = "+++++";
        final String dummyNameTwo = "-----";
        List<Genre> genreList = genreStorage.readAllItems();
        int lastSize = genreList.size();
        Genre item2Create = Genre.builder().name(dummyNameOne).build();
        final Genre item = genreStorage.createItem(item2Create);
        final GenreIdType itemId = item.getId();
        assertEquals(dummyNameOne, item.getName());
        assertEquals(1, genreStorage.readAllItems().size() - lastSize);
        assertThrows(DataAccessException.class, () -> genreStorage.createItem(item2Create));
        item.setName(dummyNameTwo);
        assertDoesNotThrow(() -> genreStorage.updateItem(item));
        assertEquals(1, genreStorage.readAllItems().size() - lastSize);
        assertDoesNotThrow(() -> genreStorage.deleteItem(itemId));
        assertEquals(lastSize, genreStorage.readAllItems().size());
        assertThrows(KeyNotFoundException.class, () -> genreStorage.deleteItem(itemId));
    }

}