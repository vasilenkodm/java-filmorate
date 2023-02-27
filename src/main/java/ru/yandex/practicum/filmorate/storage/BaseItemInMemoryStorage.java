package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseItem;
import ru.yandex.practicum.filmorate.type.SomeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
//public interface BaseItemStorage<K extends SomeType<?>, T extends BaseItem> {
public abstract class BaseItemInMemoryStorage<K extends SomeType<?>, T extends BaseItem<K, T>> implements BaseItemStorage <K, T>
{
    protected final Map<K, T> items = new TreeMap<>();

    /**
     * Генерация теста сообщения о попытке обращения к несуществующему элементу
     * @param _id Идентификатор элемента
     * @return Текст сообщения о попытке обратиться к несуществующему элементу
     */
    protected abstract String idNotFoundMsg(K _id);
    protected abstract K makeNewId();
    @Override
    public List<T> getAllItems() {
        List<T> result = new ArrayList<>(items.values());
        log.info("Выполнено {}.getAllItems()", this.getClass().getName());
        return result;
    }

    @Override
    public T readItem(K _id) {
        T result = items.get(_id);
        if (result == null) { throw new KeyNotFoundException(this.idNotFoundMsg(_id), this.getClass(), log); }
        log.info("Выполнено {}.readItem({})", this.getClass().getName(), _id);
        return result;
    }

    @Override
    public T createItem(T _item) {
        _item.getClass().new
        _item.setId(makeNewId());
        items.put(_item.getId(), _item);
        log.info("Выполнено {}.createItem({})", this.getClass().getName(), _item);
        return _item;
    }

    @Override
    public T updateItem(T _item) {
        K id = _item.getId();
        T result = items.get(id);
        if (result == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(id), this.getClass(), log);
        }
        result.updateWith(_item);
        log.info("Выполнено {}.updateItem({})", this.getClass().getName(), _item);
        return result;
    }

    @Override
    public void deleteItem(K _id) {
        if (!items.containsKey(_id)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_id), this.getClass(), log);
        }
        items.remove(_id);
        log.info("Выполнено {}.deleteItem({})", this.getClass().getName(), _id);
    }
}
