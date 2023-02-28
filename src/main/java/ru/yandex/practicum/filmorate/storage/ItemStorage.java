package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.List;

public interface ItemStorage<K extends SomeType<?>, T extends Item> {
    List<T>  getAllItems();
    T  readItem(K _id);
    T  createItem(T _item);
    T  updateItem(T _item);
    void deleteItem(K _id);
}