package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

public interface FilmStorage extends ItemStorage<FilmIdType, Film> {
    List<Film> getPopular(int _maxCount);
    int getLikesCount(FilmIdType _id);
    void addLike(FilmIdType _filmId, UserIdType _userId);
    void removeLike(FilmIdType _filmId, UserIdType _userId);
    List<Film> getSearchedFilms(String _query, Set<String> _by); //add-search
}
