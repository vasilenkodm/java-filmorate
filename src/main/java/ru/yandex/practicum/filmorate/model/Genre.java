package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.type.GenreIdType;

import javax.validation.constraints.NotBlank;

@Setter @Getter @Builder @ToString @EqualsAndHashCode
public class Genre implements BaseItem<GenreIdType, Genre> {
    private GenreIdType id;
    @NotBlank
    private String name;

    @Override
    public void updateWith(Genre _source) {
        this.name = _source.getName();
    }
}
