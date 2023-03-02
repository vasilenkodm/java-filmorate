package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.GenreIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class Genre extends BaseItem<GenreIdType, Genre> implements Item<GenreIdType, Genre> {
    Genre() {
        super();
    }

    @NotBlank
    @Size(max = 100)
    private String name;

    @Override
    public void updateWith(Genre _source) {
        this.name = _source.getName();
    }
}
