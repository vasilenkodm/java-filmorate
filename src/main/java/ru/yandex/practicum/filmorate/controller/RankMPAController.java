package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.service.RankMPAService;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

@Slf4j
@Component
@RestController
@RequestMapping("/mpa")
public class RankMPAController extends BaseController<RankMPAIdType, RankMPA> {
    public RankMPAController(RankMPAService _service) {
        super(_service);
    }
}
