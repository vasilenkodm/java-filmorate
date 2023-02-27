package ru.yandex.practicum.filmorate.type;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SomeTypeSerializer.class)
public abstract class SomeType<T extends Comparable<T>> implements java.io.Serializable, Comparable<SomeType<?>>{
    private final T value;
    public SomeType(T value) {
        this.value = value;
    }
    public T getValue() {return value; }

    @Override
    public String toString() {return value.toString();}

    @Override
    public int hashCode() {return value.hashCode();}

    @Override
    public int compareTo(SomeType<?> o) {
        return value.compareTo((T)o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass()!=this.getClass()) return false;

        return this.value.equals(((SomeType<T>)o).value);
    }
}
