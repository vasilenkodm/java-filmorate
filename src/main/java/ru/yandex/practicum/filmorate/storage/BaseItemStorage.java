package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseItem;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.List;

public interface BaseItemStorage<K extends SomeType<?>, T extends BaseItem> {
    List<T>  getAllItems();
    T  readItem(K _id);
    T  createItem(T _item);
    T  updateItem(T _item);
    void deleteItem(K _id);
}
