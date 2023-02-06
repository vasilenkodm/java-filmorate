package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.constraint.LocalDateConstraint;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    @Getter @Setter
    private FilmIdType id; //  целочисленный идентификатор — id;

    @Getter @Setter @NotBlank(message = "Название не может быть пустым!")
    private String name; //название — name;

    @Getter @Setter @NotNull(message = "Требуется указать описание!")
    @Size(max=MAX_DESCRIPTION_LENGTH, message = "Максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов!")
    private String description; //описание — description;

    @Getter @Setter @NotNull(message = "Требуется указать дату резиза!") @LocalDateConstraint(minDate= "1895-12-28", message = "Релиз не может быть ранее 28.12.1895!")
    private LocalDate releaseDate; //дата релиза — releaseDate;

    @Getter @Setter @Positive(message = "Продолжительность фильма должна быть положительной!")
    private int duration; //продолжительность фильма — duration.

    private final transient Set<UserIdType>  likers = new TreeSet<>();
    public Set<UserIdType> getLikers() {
        return Set.copyOf(likers);
    }

    public void updateWith(Film film) {
        this.name = film.name;
        this.description = film.description;
        this.releaseDate = film.releaseDate;
        this.duration = film.duration;
    }

    public void addLike(UserIdType userId) {
        likers.add(userId);
    }
    public void removeLike(UserIdType userId) {
        likers.remove(userId);
    }
    public int getRating() {
        return likers.size();
    }

}
