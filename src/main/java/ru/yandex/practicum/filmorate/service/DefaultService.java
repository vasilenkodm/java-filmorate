package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.BaseItem;
import ru.yandex.practicum.filmorate.storage.BaseItemStorage;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultService<K extends SomeType<?>, T extends BaseItem, S extends BaseItemStorage<K, T>> implements BaseItemService<K, T> {
    protected final S storage;
    
    public List<T> getAllItems() {
        log.debug("Вызов {}.getAllItems()", this.getClass().getName());
        return storage.getAllItems();
    }

    @Override
    public T readItem(final K _id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), _id);
        return storage.readItem(_id);
    }

    public T createItem(final T _item) {
        log.debug("'Вызов {}.createItem({})", this.getClass().getName(), _item);
        return storage.createItem(_item);
    }

    public T updateItem(final T _item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), _item);
        return storage.updateItem(_item);
    }

    public void deleteItem(final K _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        storage.deleteItem(_id);
    }

}
