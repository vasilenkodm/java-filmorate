package ru.yandex.practicum.filmorate.type;

public enum FilmsByDirectorSortByMode {
    YEAR,
    LIKES;

    public static FilmsByDirectorSortByMode fromString(String _val) {
        return FilmsByDirectorSortByMode.valueOf(_val.toUpperCase());
    }
}
