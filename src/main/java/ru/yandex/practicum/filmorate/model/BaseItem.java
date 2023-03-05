package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.SerializationUtils;
import ru.yandex.practicum.filmorate.exceptions.UnexpectedErrorException;

import java.io.Serializable;

@Slf4j
@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseItem<K, T extends BaseItem<K, T>> implements Serializable {
    protected K id;


    @Override
    public BaseItem<?, ?> clone() {
        try {
            return (BaseItem<?, ?>) SerializationUtils.deserialize(SerializationUtils.serialize(this));
        } catch (Exception e) {
            throw new UnexpectedErrorException(e.getMessage(), this.getClass(), log);
        }
    }

}
