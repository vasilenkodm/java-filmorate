package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest  extends CommonControllerTest<User> {
    @BeforeEach
    void initFilm() {
        item = new User(0, "user@domaim.com", "user", "name", LocalDate.now().minusYears(20));
        controller =  new UserController();
    }

    @Test
    void shouldRejectEmail() {
        item.setEmail(null);
        assertEquals(1, validator.validate(item).size());

        item.setEmail("user@");
        assertEquals(1, validator.validate(item).size());
    }

    @Test
    void shouldRejectLogin() {
        Set<ConstraintViolation<User>> violations;

        item.setLogin("");
        assertEquals(1, validator.validate(item).size());

        item.setLogin(" ");
        assertNotEquals(0, validator.validate(item).size());

        item.setLogin("The user");
        assertEquals(1, validator.validate(item).size());
    }

    @Test
    void shouldRejectBirthday() {
        item.setBirthday(LocalDate.now().minusDays(1));
        assertEquals(0, validator.validate(item).size());

        item.setBirthday(LocalDate.now().plusDays(1));
        assertEquals(1, validator.validate(item).size());
    }
}