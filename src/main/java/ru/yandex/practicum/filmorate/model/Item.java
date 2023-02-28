package ru.yandex.practicum.filmorate.model;

public interface Item<K, T extends Item> {
    K getId();

    void setId(K _id);

    void updateWith(final T _item);
}
