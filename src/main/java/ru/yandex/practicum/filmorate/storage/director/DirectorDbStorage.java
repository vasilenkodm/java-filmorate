package ru.yandex.practicum.filmorate.storage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDAO;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.DirectorIdType;

@Slf4j
@Component
@Primary
public class DirectorDbStorage extends BaseItemDbStorage<DirectorIdType, Director, DirectorDAO> implements DirectorStorage {
    public DirectorDbStorage(DirectorDAO dao) {
        super(dao);
    }
}
