package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unused")
public class StringToDirectorIdTypeConverter implements Converter<String, DirectorIdType> {
    @Override
    public DirectorIdType convert(String from) {
        return DirectorIdType.of(Integer.parseInt(from));
    }
}