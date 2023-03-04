package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.UnexpectedErrorException;

import java.io.*;

@Slf4j
@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseItem<K, T extends BaseItem<K, T>> implements Serializable {
    protected K id;

    public BaseItem<?, ?> makeCopy() {
        BaseItem<?, ?> result;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(this);
            }
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                    result = (BaseItem<?, ?>) ois.readObject();
                }
            }
        } catch (Exception ex) {
            throw new UnexpectedErrorException(ex.getMessage(), this.getClass(), log);
        }

        return result;
    }

}
