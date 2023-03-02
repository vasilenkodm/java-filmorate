package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class RankMPA extends BaseItem<RankMPAIdType, RankMPA> implements Item<RankMPAIdType, RankMPA> {
    RankMPA() {
        super();
    }

    @NotBlank
    private String name;

    @Override
    public void updateWith(RankMPA _source) {
        this.name = _source.getName();
    }
}
