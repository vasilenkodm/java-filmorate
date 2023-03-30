package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Item;

import java.util.List;

public interface ItemDAO<K, T extends Item<K, T>> {
    T create(T item);

    void update(T item);

    T read(K id);

    void delete(K id);

    List<T> selectAll();

}
