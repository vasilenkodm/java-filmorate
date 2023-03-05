package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class RankMPA extends BaseItem<RankMPAIdType, RankMPA> implements Item<RankMPAIdType, RankMPA> {
    public RankMPA() {
        super();
    }

    @NotBlank
    @Size(max = 10)
    private String name;

    @Override
    public void updateWith(RankMPA _source) {
        this.name = _source.getName();
    }
}
