package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.constraint.LocalDateConstraint;
import ru.yandex.practicum.filmorate.type.FilmIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter @Builder @ToString @EqualsAndHashCode
public class Film implements Item<FilmIdType, Film> {
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    private FilmIdType id;
    @NotBlank(message = "Название не может быть пустым!")
    private String name; //название — name;

    @NotNull(message = "Требуется указать описание!")
    @Size(max=MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!")
    private String description; //описание — description;

    @NotNull(message = "Требуется указать дату резиза!") @LocalDateConstraint(minDate= "1895-12-28", message = "Релиз не может быть ранее 28.12.1895!")
    private LocalDate releaseDate; //дата релиза — releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private int duration; //продолжительность фильма — duration.

    private RankMPA mpa;
    private List<Genre> genres;

    public void updateWith(Film film) {
        this.name = film.name;
        this.description = film.description;
        this.releaseDate = film.releaseDate;
        this.duration = film.duration;
        this.genres = (film.genres == null) ? null : new ArrayList(film.genres);
        if ((film.mpa == null)) {
            this.mpa = null;
        } else {
            this.mpa = RankMPA.builder().id(film.mpa.getId()).build();
            this.mpa.updateWith(film.mpa);
        }
    }

}
