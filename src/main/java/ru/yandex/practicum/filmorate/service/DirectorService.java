package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.type.DirectorIdType;

@Slf4j
@Service
public class DirectorService extends BaseItemService<DirectorIdType, Director, DirectorStorage> {
    public DirectorService(DirectorStorage _storage) {
        super(_storage);
    }
}
