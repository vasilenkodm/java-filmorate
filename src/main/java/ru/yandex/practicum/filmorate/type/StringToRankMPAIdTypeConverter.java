package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unused")
public class StringToRankMPAIdTypeConverter implements Converter<String, RankMPAIdType> {
    @Override
    public RankMPAIdType convert(String from) {
        return RankMPAIdType.of(Integer.parseInt(from));
    }
}