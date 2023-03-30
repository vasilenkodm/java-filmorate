package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnexpectedErrorException;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.type.ValueType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Primary
public abstract class BaseItemInMemoryStorage<K extends ValueType<?>, T extends Item<K, T>> implements ItemStorage<K, T> {
    protected final Map<K, T> items = new HashMap<>();

    /**
     * Генерация теста сообщения о попытке обращения к несуществующему элементу
     *
     * @param id Идентификатор элемента
     * @return Текст сообщения о попытке обратиться к несуществующему элементу
     */
    protected abstract String idNotFoundMsg(K id);

    protected abstract K newItemId();

    @Override
    public List<T> readAllItems() {
        List<T> result = new ArrayList<>(items.values());
        log.info("Выполнено {}.readAllItems()", this.getClass().getName());
        return result;
    }

    @Override
    public T readItem(K id) {
        T result = items.get(id);
        if (result == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(id), this.getClass(), log);
        }
        log.info("Выполнено {}.readItem({})", this.getClass().getName(), id);
        return result;
    }

    @Override
    public T createItem(T item) {
        T result;

        try {
            Method builderMethod = item.getClass().getMethod("builder");
            Object builder = builderMethod.invoke(null);
            Method buildMethod = builder.getClass().getMethod("build");
            @SuppressWarnings("unchecked")
            T tObj = (T) buildMethod.invoke(builder);
            result = tObj;
        } catch (Exception e) {
            throw new UnexpectedErrorException(e.getMessage(), this.getClass(), log);
        }
        result.setId(newItemId());
        result.updateWith(item);
        items.put(result.getId(), result);
        log.info("Выполнено {}.createItem({})", this.getClass().getName(), item);
        return result;
    }

    @Override
    public T updateItem(T item) {
        K id = item.getId();
        T result = items.get(id);
        if (result == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(id), this.getClass(), log);
        }
        result.updateWith(item);
        log.info("Выполнено {}.updateItem({})", this.getClass().getName(), item);
        return result;
    }

    @Override
    public void deleteItem(K id) {
        if (!items.containsKey(id)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(id), this.getClass(), log);
        }
        items.remove(id);
        log.info("Выполнено {}.deleteItem({})", this.getClass().getName(), id);
    }
}
