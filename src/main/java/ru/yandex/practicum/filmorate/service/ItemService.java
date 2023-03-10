package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

public interface ItemService<K extends ValueType<?>, T extends Item<K, T>> {
    List<T> readAllItems();

    T readItem(K _id);

    T createItem(T _item);

    T updateItem(T _item);

    void deleteItem(K _id);
}
