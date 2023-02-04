package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<FilmIdType, Film> films;

    private final Map<FilmIdType, Set<UserIdType>> likes;
    private final TreeMap<Integer, Set<FilmIdType>> raiting;

    public InMemoryFilmStorage() {
        this.films = new TreeMap<>();
        this.likes =  new TreeMap<>();
        this.raiting = new TreeMap<>();
    }
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
    public boolean exits(FilmIdType filmId) {
        return films.containsKey(filmId);
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
        addFilmToRaiting(film.getId(), 0);
    }

    public void updateFilm(Film film) {
        films.replace(film.getId(), film);
    }
    public Film getFilm(FilmIdType filmId) {
        return films.get(filmId);
    }

    private Set<UserIdType> getLikesSet(FilmIdType filmId) {
        Set<UserIdType> likesSet = likes.get(filmId);
        if (likesSet == null) {
            likesSet =  new TreeSet<>();
            likes.put(filmId, likesSet);
        }
        return likesSet;
    }

    private void removeFilmFromRaiting(FilmIdType filmId, int likeCount) {
        raiting.get(-likeCount).remove(filmId);
    }

    private void addFilmToRaiting(FilmIdType filmId, int likeCount) {
        if (raiting.containsKey(-likeCount)) {
            raiting.get(-likeCount).add(filmId);
        } else {
            Set<FilmIdType> positionSet = new TreeSet<>();
            positionSet.add(filmId);
            raiting.put(-likeCount, positionSet);
        }
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> likesSet = getLikesSet(filmId);

        removeFilmFromRaiting(filmId, likesSet.size());
        likesSet.add(userId);
        addFilmToRaiting(filmId, likesSet.size());
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> likesSet = getLikesSet(filmId);

        removeFilmFromRaiting(filmId, likesSet.size());
        likesSet.remove(userId);
        addFilmToRaiting(filmId, likesSet.size());
    }

    public Set<Film> getPopular(int maxCount) {
        Set<Film> result = new LinkedHashSet<>(maxCount);
        for (Set<FilmIdType> filmSet : raiting.values()) {
            for (FilmIdType id: filmSet) {
                result.add(films.get(id));
                if (result.size()>=maxCount) { break; }
            }
            if (result.size()>=maxCount) { break; }
        }
        return result;
    }

}
