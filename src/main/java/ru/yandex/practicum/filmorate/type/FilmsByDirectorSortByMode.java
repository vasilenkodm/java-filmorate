package ru.yandex.practicum.filmorate.type;

public enum FilmsByDirectorSortByMode {
    YEAR,
    LIKES;

    public static FilmsByDirectorSortByMode fromString(String val) {
        return FilmsByDirectorSortByMode.valueOf(val.toUpperCase());
    }
}
