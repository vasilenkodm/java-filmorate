package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.service.RankMPAService;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RankMPAController extends BaseItemController<RankMPAIdType, RankMPA, RankMPAService> {
    public RankMPAController(RankMPAService service) {
        super(service);
    }
}
