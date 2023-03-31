package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import ru.yandex.practicum.filmorate.exceptions.UnexpectedErrorException;
import ru.yandex.practicum.filmorate.type.ReviewIdType;

import javax.validation.constraints.NotNull;

@Slf4j
@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Review implements Item<ReviewIdType, Review> {
    public Review() {
        super();
    }

    private ReviewIdType reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;
    private int useful;

    @Override
    public ReviewIdType getId() {
        return getReviewId();
    }

    @Override
    public void setId(ReviewIdType id) {
        setReviewId(id);
    }

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

    public Review clone() {
        try {
            return (Review) SerializationUtils.deserialize(SerializationUtils.serialize(this));
        } catch (Exception e) {
            throw new UnexpectedErrorException(e.getMessage(), this.getClass(), log);
        }
    }
}
