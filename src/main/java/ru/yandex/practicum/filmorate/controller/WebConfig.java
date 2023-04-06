package ru.yandex.practicum.filmorate.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.practicum.filmorate.type.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToUserIdTypeConverter());
        registry.addConverter(new StringToFilmIdTypeConverter());
        registry.addConverter(new StringToGenreIdTypeConverter());
        registry.addConverter(new StringToRankMPAIdTypeConverter());
        registry.addConverter(new StringToDirectorIdTypeConverter());
        registry.addConverter(new StringToReviewIdTypeConverter());
    }

}