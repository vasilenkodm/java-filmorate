package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.storage.rankmpa.RankMPAStorage;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@Service
public class RankMPAService extends BaseItemService<RankMPAIdType, RankMPA, RankMPAStorage> {
    RankMPAService(RankMPAStorage storage) {
        super(storage);
    }
}
