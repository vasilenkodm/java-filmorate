package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.constraint.LocalDateConstraint;
import ru.yandex.practicum.filmorate.type.FilmIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseItem<FilmIdType, Film> implements Item<FilmIdType, Film> {
    Film() {
        super();
    }

    public static final int MAX_DESCRIPTION_LENGTH = 200;

    @NotBlank(message = "Название не может быть пустым!")
    private String name; //название — name;

    @NotNull(message = "Требуется указать описание!")
    @Size(max = MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!")
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
        this.genres = (film.genres == null) ? null : new ArrayList<>(film.genres);
        if ((film.mpa == null)) {
            this.mpa = null;
        } else {
            this.mpa = film.mpa.toBuilder().id(film.mpa.getId()).build();
        }
    }

}
