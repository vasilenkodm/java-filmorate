package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest  extends CommonControllerTest<User> {

    @BeforeEach
    void initFilm() {
        item = new User(1, "user@domaim.com", "user", "name", LocalDate.of(2000, 12, 31));
        controller =  new UserController();
    }

    @Test
    void shouldRejectEmail() {
        item.setEmail("");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setEmail("user@com");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setEmail("user@");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setEmail("@com");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setEmail("usercom");
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

    @Test
    void shouldRejectLogin() {
        item.setLogin("");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setLogin(" ");
        assertThrows(ValidationException.class, () -> controller.validate(item));
        item.setLogin("The user");
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

    @Test
    void shouldRejectBirthday() {
        item.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> controller.validate(item));
        item.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> controller.validate(item));
    }

}