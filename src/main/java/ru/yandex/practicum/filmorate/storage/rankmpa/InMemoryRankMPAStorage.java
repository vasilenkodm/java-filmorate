package ru.yandex.practicum.filmorate.storage.rankmpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@Component
@Primary
public class InMemoryRankMPAStorage extends BaseItemInMemoryStorage<RankMPAIdType, RankMPA> implements RankMPAStorage {
    private RankMPAIdType lastId = RankMPAIdType.of(0);

    @Override
    protected RankMPAIdType newItemId() {
        lastId = RankMPAIdType.of(lastId.getValue() + 1);
        return lastId;
    }

    @Override
    protected String idNotFoundMsg(RankMPAIdType _id) {
        return String.format("Не найден рейтинг MPA с кодом %s!", _id);
    }

}
