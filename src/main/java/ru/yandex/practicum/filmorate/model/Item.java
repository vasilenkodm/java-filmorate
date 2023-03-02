package ru.yandex.practicum.filmorate.model;

public interface Item<K, T extends Item<K, T>> {
    K getId();

    void setId(K _id);

    default void updateWith(final T _item) {
    }
}
