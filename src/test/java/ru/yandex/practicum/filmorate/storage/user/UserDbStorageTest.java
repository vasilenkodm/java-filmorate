package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @Order(1)
    void testUserStorage() {
        final String loginOne = Instant.now().toString();
        final String loginTwo = loginOne + "+";
        List<User> userList = userStorage.readAllItems();
        int lastSize = userList.size();

        User user2create = User.builder().name(loginOne).email(loginOne).login(loginOne).birthday(LocalDate.now().minusYears(1)).build();
        User user = userStorage.createItem(user2create);

        final UserIdType userId = user.getId();

        assertEquals(loginOne, user.getName());
        assertEquals(1, userStorage.readAllItems().size() - lastSize);
        assertThrows(DataAccessException.class, () -> userStorage.createItem(user2create));
        user.setName(loginTwo);
        assertDoesNotThrow(() -> userStorage.updateItem(user));
        assertEquals(1, userStorage.readAllItems().size() - lastSize);

        assertDoesNotThrow(() -> userStorage.deleteItem(userId));
        assertEquals(lastSize, userStorage.readAllItems().size());
        assertThrows(KeyNotFoundException.class, () -> userStorage.deleteItem(userId));
    }

    @Test
    @Order(2)
    void testUserStorageWithFriends() {
        final String dummyNameOne = Instant.now().toString();
        final String dummyNameTwo = dummyNameOne + "+";
        final String dummyNameThree = dummyNameOne + "-";

        User userOne = User.builder().name(dummyNameOne).email(dummyNameOne).login(dummyNameOne).birthday(LocalDate.now().minusYears(1)).build();
        User userTwo = User.builder().name(dummyNameTwo).email(dummyNameTwo).login(dummyNameTwo).birthday(LocalDate.now().minusYears(1)).build();
        User userThree = User.builder().name(dummyNameThree).email(dummyNameThree).login(dummyNameThree).birthday(LocalDate.now().minusYears(1)).build();

        userOne = userStorage.createItem(userOne);
        userTwo = userStorage.createItem(userTwo);
        userThree = userStorage.createItem(userThree);

        final UserIdType userIdOne = userOne.getId();
        final UserIdType userIdTwo = userTwo.getId();
        final UserIdType userIdThree = userThree.getId();
        final UserIdType fakeUserId = UserIdType.of(-999L);

        assertNotEquals(userOne.getId(), userTwo.getId());
        assertNotEquals(userOne.getId(), userThree.getId());
        assertEquals(0, userStorage.getFriends(userIdOne).size());
        assertEquals(0, userStorage.getFriends(userIdTwo).size());
        assertEquals(0, userStorage.getFriends(userIdThree).size());

        assertThrows(KeyNotFoundException.class, () -> userStorage.addFriend(userIdOne, userIdOne));

        assertDoesNotThrow(() -> userStorage.addFriend(userIdOne, userIdThree));
        assertEquals(1, userStorage.getFriends(userIdOne).size());
        assertEquals(0, userStorage.getFriends(userIdTwo).size());
        assertEquals(0, userStorage.commonFriends(userIdOne, userIdTwo).size());
        assertDoesNotThrow(() -> userStorage.addFriend(userIdTwo, userIdThree));
        assertEquals(1, userStorage.commonFriends(userIdOne, userIdTwo).size());

        assertEquals(0, userStorage.getFriends(userIdThree).size());
        assertDoesNotThrow(() -> userStorage.addFriend(userIdThree, userIdOne));
        assertEquals(1, userStorage.getFriends(userIdThree).size());
        assertDoesNotThrow(() -> userStorage.removeFriend(userIdThree, userIdOne));
        assertEquals(0, userStorage.getFriends(userIdThree).size());

        assertThrows(KeyNotFoundException.class, () -> userStorage.addFriend(userIdOne, fakeUserId));
        assertThrows(KeyNotFoundException.class, () -> userStorage.removeFriend(userIdOne, fakeUserId));
    }

}