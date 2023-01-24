package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Item;

import org.slf4j.Logger;

import java.util.*;

@RestController
public abstract class CommonController<T extends Item> {
    protected Logger log;
    private int lastId;
    private int getnewId() {return ++lastId;}

    private final Map<Integer, T> itemsMap = new TreeMap<>();

    @GetMapping()
    public List<T> getAllItems() {
        return new ArrayList<>(itemsMap.values());
    }

    @PostMapping
    public T create(@RequestBody final T item) {
        try {
            validate(item);
        } catch (ValidationException e) {
            log.error("{} Сбой добавления. {}",e.getMessage(), item);
            throw e;
        }


        int key = getnewId();
        item.setId(key);
        if (!itemsMap.containsKey(key)) {
            log.info("Добавление {}", item);
            itemsMap.put(key, item);
        }
        return item;
    }
    @PutMapping
    public T update(@RequestBody final T item) {
        try {
            validate(item);
        } catch (ValidationException e) {
            log.error("{} Сбой обновления. {}",e.getMessage(), item);
            throw e;
        }

        int key = item.getId();
        if (itemsMap.containsKey(key)) {
            log.info("Обновление {}", item);
            itemsMap.replace(key, item);
        } else {
            ValidationException exception = new ValidationException("Не нейден ключ "+key+"!");
            log.error("{} Сбой обновления. {}",exception.getMessage(), item);
            throw exception;
        }

        return item;
    }

    @DeleteMapping
    public void remove(@RequestBody T item) {
        itemsMap.remove(item.getId());
        log.info("Удаление {}", item);
    }

    public abstract void validate(T item) throws ValidationException;

}
