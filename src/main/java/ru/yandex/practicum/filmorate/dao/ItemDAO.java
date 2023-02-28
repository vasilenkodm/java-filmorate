package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;

public interface ItemDAO<K, T extends Item> {
    K create(T _item);
    void update(T _item);
    T read(K _id);
    void delete(K _id);
    List<T> selectAll();
}
