package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.GenreIdType;

@Slf4j
@Component
@Primary
public class InMemoryGenreStorage extends BaseItemInMemoryStorage<GenreIdType, Genre> implements GenreStorage {
    private GenreIdType lastId = GenreIdType.of(0);

    @Override
    protected GenreIdType newItemId() {
        lastId = GenreIdType.of(lastId.getValue() + 1);
        return lastId;
    }

    @Override
    protected String idNotFoundMsg(GenreIdType _id) {
        return String.format("Не найден жанр с кодом %s!", _id);
    }

}
