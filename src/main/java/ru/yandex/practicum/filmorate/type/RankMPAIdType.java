package ru.yandex.practicum.filmorate.type;

public final class RankMPAIdType extends ValueType<Integer> {
    public RankMPAIdType(Integer value) {
        super(value);
    }

    public static RankMPAIdType of(Integer value) {
        return new RankMPAIdType(value);
    }
}
