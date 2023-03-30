package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ItemDAO;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public abstract class BaseItemDbStorage<K extends ValueType<?>, T extends Item<K, T>, D extends ItemDAO<K, T>> implements ItemStorage<K, T> {
    protected final D dao;

    @Override
    public List<T> readAllItems() {
        log.debug("Вызов {}.readAllItems()", this.getClass().getName());
        return dao.selectAll();
    }

    @Override
    public T readItem(K id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), id);
        return dao.read(id);
    }

    @Override
    public T createItem(T item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), item);
        return dao.create(item);
    }

    @Override
    public T updateItem(T item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), item);
        dao.update(item);
        return dao.read(item.getId());
    }

    @Override
    public void deleteItem(K id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), id);
        dao.delete(id);
    }
}
