package ru.yandex.practicum.filmorate.type;

import org.springframework.core.convert.converter.Converter;

public class StringToUserIdTypeConverter
  implements Converter<String, UserIdType> {

    @Override
    public UserIdType convert(String from) {
        return new UserIdType(Long.parseLong(from));
    }

}