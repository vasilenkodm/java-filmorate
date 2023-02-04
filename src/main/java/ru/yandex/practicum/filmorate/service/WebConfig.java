package ru.yandex.practicum.filmorate.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.StringToFilmIdTypeConverter;
import ru.yandex.practicum.filmorate.type.StringToUserIdTypeConverter;
import ru.yandex.practicum.filmorate.type.UserIdType;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new StringToUserIdTypeConverter());
        registry.addConverter(new StringToFilmIdTypeConverter());
    }
}