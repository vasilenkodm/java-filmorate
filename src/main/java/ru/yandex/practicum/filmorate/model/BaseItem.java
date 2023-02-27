package ru.yandex.practicum.filmorate.model;

public interface BaseItem<K, T extends BaseItem> {
    K getId();
    void setId(K _id);
    void updateWith(T _item);

}
