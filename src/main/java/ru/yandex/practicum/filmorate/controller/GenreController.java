package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.type.GenreIdType;

@Slf4j
@Component
@RestController
@RequestMapping("/genres")
public class GenreController extends BaseController<GenreIdType, Genre> {
    public GenreController(GenreService _service) {
        super(_service);
    }
}
