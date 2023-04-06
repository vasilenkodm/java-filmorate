package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.storage.rankmpa.RankMPAStorage;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RankMPADbStorageTest {
    private final RankMPAStorage mpaStorage;

    @Test
    @Order(1)
    void testMPAStorage() {
        final String dummyNameOne = "+++++";
        final String dummyNameTwo = "-----";
        List<RankMPA> mpaList = mpaStorage.readAllItems();
        int lastSize = mpaList.size();
        RankMPA item2Create = RankMPA.builder().name(dummyNameOne).build();
        RankMPA item = mpaStorage.createItem(item2Create);
        final RankMPAIdType itemId = item.getId();

        assertEquals(dummyNameOne, item.getName());
        assertEquals(1, mpaStorage.readAllItems().size() - lastSize);
        assertThrows(DataAccessException.class, () -> mpaStorage.createItem(item2Create));
        item.setName(dummyNameTwo);

        assertDoesNotThrow(() -> mpaStorage.updateItem(item));
        assertEquals(1, mpaStorage.readAllItems().size() - lastSize);
        assertDoesNotThrow(() -> mpaStorage.deleteItem(itemId));
        assertEquals(lastSize, mpaStorage.readAllItems().size());
        assertThrows(KeyNotFoundException.class, () -> mpaStorage.deleteItem(itemId));
    }

}