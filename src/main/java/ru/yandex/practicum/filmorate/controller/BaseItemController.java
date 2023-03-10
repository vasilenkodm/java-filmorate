package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Item;
import ru.yandex.practicum.filmorate.service.ItemService;
import ru.yandex.practicum.filmorate.type.ValueType;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseItemController<K extends ValueType<?>, T extends Item<K, T>, S extends ItemService<K, T>> {
    protected final S service;

    @GetMapping()
    public List<T> readAllItems() {
        log.debug("Вызов {}.readAllItems()", this.getClass().getName());
        return service.readAllItems();
    }

    @GetMapping("/{_id}")
    public T readItem(@PathVariable final K _id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), _id);
        return service.readItem(_id);
    }

    @PostMapping
    public T createItem(@Valid @RequestBody final T _item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), _item);
        return service.createItem(_item);
    }

    @PutMapping
    public T updateItem(@Valid @RequestBody final T _item) {
        log.debug("Вызов {}.updateItem({})", this.getClass().getName(), _item);
        return service.updateItem(_item);
    }

    @DeleteMapping("/{_id}")
    public void deleteItem(@PathVariable final K _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        service.deleteItem(_id);
    }

}
