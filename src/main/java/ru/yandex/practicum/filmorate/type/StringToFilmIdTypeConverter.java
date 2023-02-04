package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

public class StringToFilmIdTypeConverter
  implements Converter<String, FilmIdType> {

    @Override
    public FilmIdType convert(String from) {
        return new FilmIdType(Long.parseLong(from));
    }

}