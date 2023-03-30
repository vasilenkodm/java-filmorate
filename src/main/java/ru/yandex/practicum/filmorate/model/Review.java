package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.ReviewIdType;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseItem<ReviewIdType, Review> implements Item<ReviewIdType, Review> {
    public Review() { super();}

    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;
    private int useful;

    @Override
    public void updateWith(Review item) {
        this.content = item.content;
        this.isPositive = item.isPositive;
        this.userId = item.userId;
        this.filmId = item.filmId;
        this.useful = item.useful;
    }

    public Boolean getIsPositive() {
        return isPositive;
    }
}
