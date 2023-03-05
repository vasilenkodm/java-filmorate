package ru.yandex.practicum.filmorate.storage.rankmpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RankMPADAO;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@Component
@Primary
public class RankMPADbStorage extends BaseItemDbStorage<RankMPAIdType, RankMPA, RankMPADAO> implements RankMPAStorage {
    public RankMPADbStorage(RankMPADAO dao) {
        super(dao);
    }
}
