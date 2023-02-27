package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseItem;
import ru.yandex.practicum.filmorate.service.BaseItemService;
import ru.yandex.practicum.filmorate.type.SomeType;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseController<K extends SomeType<?>, T extends BaseItem> {
    private final BaseItemService<K, T>  service;

    @GetMapping()
    public List<T> getAllItems() {
        log.debug("Вызов {}.getAllItems()", this.getClass().getName());
        return service.getAllItems();
    }

    @GetMapping("/{_id}")
    public T readItem(@PathVariable final K _id) {
        log.debug("Вызов {}.readItem({})", this.getClass().getName(), _id);
        return service.readItem(_id);
    }

    @PostMapping
    public T createItem(@Valid @RequestBody final T _item) {
        log.debug("'Вызов {}.createItem({})", this.getClass().getName(), _item);
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
