package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.DirectorIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class Director extends BaseItem<DirectorIdType, Director> implements Item<DirectorIdType, Director> {
    public Director() {
        super();
    }

    @NotBlank
    @Size(max = 100)
    private String name;

    @Override
    public void updateWith(Director item) {
        this.name = item.getName();
    }
}
