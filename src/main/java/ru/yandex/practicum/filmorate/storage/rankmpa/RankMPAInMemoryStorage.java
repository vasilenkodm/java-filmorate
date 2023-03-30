package ru.yandex.practicum.filmorate.storage.rankmpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@Component
public class RankMPAInMemoryStorage extends BaseItemInMemoryStorage<RankMPAIdType, RankMPA> implements RankMPAStorage {
    private RankMPAIdType lastId = RankMPAIdType.of(0);

    @Override
    protected RankMPAIdType newItemId() {
        lastId = RankMPAIdType.of(lastId.getValue() + 1);
        return lastId;
    }

    @Override
    protected String idNotFoundMsg(RankMPAIdType id) {
        return String.format("Не найден рейтинг MPA с кодом %s!", id);
    }

}
