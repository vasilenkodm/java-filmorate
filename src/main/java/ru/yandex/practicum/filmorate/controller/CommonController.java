package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Item;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public abstract class CommonController<T extends Item> {
    private int lastId;
    private int getnewId() {return ++lastId;}

    private final Map<Integer, T> itemsMap = new TreeMap<>();

    @GetMapping()
    public List<T> getAllItems() {
        return new ArrayList<>(itemsMap.values());
    }

    @PostMapping
    public T create(@Valid @RequestBody final T item) {
        int key = getnewId();
        item.setId(key);
        if (!itemsMap.containsKey(key)) {
            itemsMap.put(key, item);
        }
        log.info("Добавление {}", item);
        return item;
    }
    @PutMapping
    public T update(@Valid @RequestBody final T item) {
        int key = item.getId();

        if (itemsMap.containsKey(key)) {
            itemsMap.replace(key, item);
        } else {
            throw new KeyNotFoundException("Обновление: не найден ключ "+key+"! "+item.toString(), item.getClass(), log);
        }

        log.info("Обновление {}", item);
        return item;
    }

    @DeleteMapping
    public void remove(@RequestBody T item) {
        int key = item.getId();

        if (itemsMap.containsKey(key)) {
            itemsMap.remove(item.getId());
        } else {
            throw new KeyNotFoundException("Удаление: не найден ключ "+key+"! "+item.toString(), item.getClass(), log);
        }

        log.info("Удаление {}", item);
    }

}
