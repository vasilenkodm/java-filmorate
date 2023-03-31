package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unused")
public class StringToReviewIdTypeConverter implements Converter<String, ReviewIdType> {
    @Override
    public ReviewIdType convert(String from) {
        return ReviewIdType.of(Long.parseLong(from));
    }
}
