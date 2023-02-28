package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import javax.validation.constraints.NotBlank;

@Setter @Getter @Builder @ToString @EqualsAndHashCode
public class RankMPA implements Item<RankMPAIdType, RankMPA> {
    private RankMPAIdType id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Override
    public void updateWith(RankMPA _source) {
        this.name = _source.getName();
        this.description = _source.getDescription();
    }
}
