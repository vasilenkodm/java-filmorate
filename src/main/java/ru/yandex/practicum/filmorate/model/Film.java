package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.constraint.LocalDateConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Film implements Item {
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    public static final String LocalDateS  = "";

    @Getter @Setter
    private int id; //  целочисленный идентификатор — id;

    @Getter @Setter @NotBlank(message = "Название не может быть пустым!")
    private String name; //название — name;

    @Getter @Setter @NotNull(message = "Требуется указать описание!") @Size(max=MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!")
    private String description; //описание — description;

    @Getter @Setter @NotNull(message = "Требуется указать дату резиза!") @LocalDateConstraint(minDate= "1895-12-28", message = "Релиз не может быть ранее 28.12.1895!")
    private LocalDate releaseDate; //дата релиза — releaseDate;

    @Getter @Setter @Positive(message = "Продолжительность фильма должна быть положительной!")
    private int duration; //продолжительность фильма — duration.
}
