package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.BaseItem;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.List;

public interface BaseItemService<Key extends SomeType<?>, Item extends BaseItem> {
    List<Item> getAllItems();
    Item readItem(Key _id);
    Item createItem(Item _item);
    Item updateItem(Item _item);
    void deleteItem(Key _id);
}
