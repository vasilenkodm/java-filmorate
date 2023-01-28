package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Item;

import javax.validation.Validation;
import javax.validation.Validator;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

abstract class CommonControllerTest<T extends Item> {
    protected T item;
    protected CommonController<T> controller;

    protected static Validator validator;
    @BeforeAll
    public static void beforeAll() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testItemOperations() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T blankItem = (T)item.getClass().getConstructor().newInstance();

        assertEquals(0, validator.validate(item).size(), "Объект неправильно инициализирован");

        assertNotEquals(0, validator.validate(blankItem).size());
        blankItem.setId(-1*item.getId());
        assertNotEquals(0, validator.validate(blankItem).size());
        assertThrows(KeyNotFoundException.class, ()->controller.update(blankItem));

        assertEquals(0, controller.getAllItems().size());

        assertDoesNotThrow(()->controller.create(item));
        assertEquals(1, controller.getAllItems().size());


        assertDoesNotThrow(()->controller.update(item));
        assertEquals(1, controller.getAllItems().size());

        assertNotEquals(0, validator.validate(blankItem).size());

        assertThrows(KeyNotFoundException.class, ()->controller.remove(blankItem));
        assertEquals(1, controller.getAllItems().size());

        assertDoesNotThrow(()->controller.remove(item));
        assertEquals(0, controller.getAllItems().size());


    }

}
