package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

@JsonSerialize(using = AbstractTypeSerializer.class)
public abstract class AbstractType<T extends Comparable<T>> implements java.io.Serializable, Comparable<AbstractType<?>>{
    private final T value;
    public AbstractType(T value) {
        this.value = value;
    }
    public T getValue() {return value; }

    @Override
    public String toString() {return value.toString();}

    @Override
    public int hashCode() {return value.hashCode();}

    @Override
    public int compareTo(AbstractType<?> o) {
        return value.compareTo((T)o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass()!=this.getClass()) return false;

        return this.value.equals(((AbstractType<T>)o).value);
    }
}
