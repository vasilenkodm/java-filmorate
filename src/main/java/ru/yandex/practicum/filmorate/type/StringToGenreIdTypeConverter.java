package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unused")
public class StringToGenreIdTypeConverter implements Converter<String, GenreIdType> {
    @Override
    public GenreIdType convert(String from) {
        return GenreIdType.of(Integer.parseInt(from));
    }
}