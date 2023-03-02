package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SomeTypeSerializer.class)
public abstract class SomeType<T extends Comparable<T>> implements java.io.Serializable, Comparable<SomeType<T>> {
    private final T value;

    public SomeType(T value) {
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
    public int compareTo(SomeType<T> o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) return false;

        @SuppressWarnings("unchecked")
        SomeType<T> bro = (SomeType<T>) o;
        return this.value.equals(bro.value);
    }
}
