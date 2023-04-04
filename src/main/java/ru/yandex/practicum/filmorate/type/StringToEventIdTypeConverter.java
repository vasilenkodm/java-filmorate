package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unused")
public class StringToEventIdTypeConverter implements Converter<String, EventIdType> {
    @Override
    public EventIdType convert(String from) {
        return new EventIdType(Long.parseLong(from));
    }
}