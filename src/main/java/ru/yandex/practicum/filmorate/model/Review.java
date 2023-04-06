package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import ru.yandex.practicum.filmorate.exceptions.UnexpectedErrorException;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.ReviewIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

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
    private UserIdType userId;

    @NotNull
    private FilmIdType filmId;
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

    public Review clone() {
        try {
            return (Review) SerializationUtils.deserialize(SerializationUtils.serialize(this));
        } catch (Exception e) {
            throw new UnexpectedErrorException(e.getMessage(), this.getClass(), log);
        }
    }
}
