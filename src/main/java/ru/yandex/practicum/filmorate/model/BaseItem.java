package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode
abstract class BaseItem<K, T extends Item> implements Item<K, T> {
    protected K id;

    BaseItem() {
    }
}
