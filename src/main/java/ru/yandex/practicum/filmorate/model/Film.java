package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Film implements Item {
    private int id; //  целочисленный идентификатор — id;

    private String name; //название — name;

    private String description; //описание — description;

    private LocalDate releaseDate; //дата релиза — releaseDate;

    private Integer duration; //продолжительность фильма — duration.
}
