package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

public interface ItemService<K extends ValueType<?>, T extends Item<K, T>> {
    List<T> readAllItems();

    T readItem(K id);

    T createItem(T item);

    T updateItem(T item);

    void deleteItem(K id);
}
