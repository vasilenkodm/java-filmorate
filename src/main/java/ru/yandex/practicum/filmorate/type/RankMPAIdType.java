package ru.yandex.practicum.filmorate.type;

public class RankMPAIdType extends SomeType<Integer> {
    public RankMPAIdType(Integer value) { super(value); }
    public static RankMPAIdType of(final Integer _value) { return new RankMPAIdType(_value); }
}
