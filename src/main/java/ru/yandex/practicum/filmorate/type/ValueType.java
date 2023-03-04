package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;

@JsonSerialize(using = ValueTypeSerializer.class)
public abstract class ValueType<T extends Comparable<T>> implements Serializable, Comparable<ValueType<T>> {
    private final T value;

    protected ValueType(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {return value.hashCode();}

    @Override
    public int compareTo(ValueType<T> o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ValueType)) {
            return false;
        }

        ValueType<?> other = (ValueType<?>) o;
        return Objects.equals(this.value, other.value);
    }
}
