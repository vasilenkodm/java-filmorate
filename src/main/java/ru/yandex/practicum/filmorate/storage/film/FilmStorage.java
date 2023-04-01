package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.List;
import java.util.Set;

public interface FilmStorage extends ItemStorage<FilmIdType, Film> {
    List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year);

    default List<Film> getPopular(int maxCount) {
        return getPopular(maxCount, null, null);
    }

    int getLikesCount(FilmIdType id);

    void addLike(FilmIdType filmId, UserIdType userId);

    void removeLike(FilmIdType filmId, UserIdType userId);

    List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy);

    List<Film> getSearchedFilms(String query, Set<String> by);
}
