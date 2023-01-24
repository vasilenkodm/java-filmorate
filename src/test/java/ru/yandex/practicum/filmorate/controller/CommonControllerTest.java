package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Item;

import static org.junit.jupiter.api.Assertions.*;

abstract class CommonControllerTest<T extends Item> {
    protected T item;
    protected CommonController<T> controller;

    @Test
    void shouldValidateDefaultItem() {
        assertDoesNotThrow(()->controller.validate(item));
    }
    @Test
    void testItemOperations() {
        assertEquals(0, controller.getAllItems().size());
        assertDoesNotThrow(()->controller.create(item));
        assertEquals(1, controller.getAllItems().size());
        assertDoesNotThrow(()->controller.update(item));
        assertEquals(1, controller.getAllItems().size());
        controller.remove(item);
        assertEquals(0, controller.getAllItems().size());
    }

}
