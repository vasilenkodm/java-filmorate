package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.GenreIdType;

@Slf4j
@Component
@Primary
public class GenreDbStorage extends BaseItemDbStorage<GenreIdType, Genre, GenreDAO> implements GenreStorage {
    public GenreDbStorage(GenreDAO dao) {
        super(dao);
    }
}
