package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.type.GenreIdType;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController extends BaseItemController<GenreIdType, Genre, GenreService> {
    public GenreController(GenreService service) {
        super(service);
    }
}
