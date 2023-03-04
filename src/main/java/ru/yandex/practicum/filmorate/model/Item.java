package ru.yandex.practicum.filmorate.model;

import java.io.Serializable;

public interface Item<K, T extends Item<K, T>> extends Serializable {
    K getId();

    void setId(K _id);

    default void updateWith(final T _item) {
    }
}
