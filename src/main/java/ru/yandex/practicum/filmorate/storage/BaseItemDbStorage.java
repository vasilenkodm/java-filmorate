package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ItemDAO;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
//public interface BaseItemStorage<K extends SomeType<?>, T extends BaseItem> {
public abstract class BaseItemDbStorage<K extends SomeType<?>, T extends Item<K, T>> implements ItemStorage<K, T>
{
    private final ItemDAO<K, T> dao;

    @Override
    public List<T> getAllItems() {
        log.debug("Вызов {}.getAllItems()", this.getClass().getName());
        return dao.selectAll();
    }

    @Override
    public T readItem(K _id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), _id);
        return dao.read(_id);
    }

    @Override
    public T createItem(T _item) {
        log.debug("'Вызов {}.createItem({})", this.getClass().getName(), _item);
        K id = dao.create(_item);
        return dao.read(id);
    }

    @Override
    public T updateItem(T _item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), _item);
        dao.update(_item);
        return dao.read((K)_item.getId());
    }

    @Override
    public void deleteItem(K _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        dao.delete(_id);
    }
}
