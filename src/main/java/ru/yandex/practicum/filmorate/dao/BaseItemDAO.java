package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.BaseItem;

import java.util.List;

public interface BaseItemDAO<K, T extends BaseItem> {
    K create(T _item);
    void update(T _item);
    T read(K _id);
    void delete(K _id);
    List<T> selectAll();
}
