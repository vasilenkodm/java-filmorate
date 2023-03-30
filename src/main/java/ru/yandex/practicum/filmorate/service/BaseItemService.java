package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class BaseItemService<K extends ValueType<?>, T extends Item<K, T>, S extends ItemStorage<K, T>> implements ItemService<K, T> {
    protected final S storage;

    public List<T> readAllItems() {
        log.debug("Вызов {}.readAllItems()", this.getClass().getName());
        return storage.readAllItems();
    }

    @Override
    public T readItem(final K id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), id);
        return storage.readItem(id);
    }

    public T createItem(final T item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), item);
        return storage.createItem(item);
    }

    public T updateItem(final T item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), item);
        return storage.updateItem(item);
    }

    public void deleteItem(final K id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), id);
        storage.deleteItem(id);
    }

}
