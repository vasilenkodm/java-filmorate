package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<FilmIdType, Film> films;

    private final Map<FilmIdType, Set<UserIdType>> likes;

    private final TreeMap<Integer, Set<FilmIdType>> rating;

    public InMemoryFilmStorage() {
        this.films = new TreeMap<>();
        this.likes =  new TreeMap<>();
        this.rating = new TreeMap<>();
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public boolean notExits(FilmIdType filmId) {
        return !films.containsKey(filmId);
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
        addFilmToRating(film.getId(), 0);
    }

    public void updateFilm(Film film) {
        films.replace(film.getId(), film);
    }

    public Film getFilm(FilmIdType filmId) {
        return films.get(filmId);
    }

    private Set<UserIdType> getLikesSet(FilmIdType filmId) {
        return likes.computeIfAbsent(filmId, k -> new TreeSet<>());
    }

    private void removeFilmFromRating(FilmIdType filmId, int likeCount) {
        rating.get(-likeCount).remove(filmId);
    }

    private void addFilmToRating(FilmIdType filmId, int likeCount) {
        if (rating.containsKey(-likeCount)) {
            rating.get(-likeCount).add(filmId);
        } else {
            Set<FilmIdType> positionSet = new TreeSet<>();
            positionSet.add(filmId);
            rating.put(-likeCount, positionSet);
        }
    }

    public void addLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> likesSet = getLikesSet(filmId);
        removeFilmFromRating(filmId, likesSet.size());
        likesSet.add(userId);
        addFilmToRating(filmId, likesSet.size());
    }

    public void removeLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> likesSet = getLikesSet(filmId);
        removeFilmFromRating(filmId, likesSet.size());
        likesSet.remove(userId);
        addFilmToRating(filmId, likesSet.size());
    }

    public Set<Film> getPopular(int maxCount) {
        Set<Film> result = new LinkedHashSet<>(maxCount);
        for (Set<FilmIdType> filmSet : rating.values()) {
            for (FilmIdType id: filmSet) {
                result.add(films.get(id));
                if (result.size()>=maxCount) { break; }
            }
            if (result.size()>=maxCount) { break; }
        }
        return result;
    }
}
