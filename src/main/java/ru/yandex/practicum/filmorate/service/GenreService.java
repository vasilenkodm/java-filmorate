package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.type.GenreIdType;

@Slf4j
@Service
public class GenreService extends DefaultService<GenreIdType, Genre, GenreStorage> {
    GenreService(GenreStorage _storage) {
        super(_storage);
    }
}
