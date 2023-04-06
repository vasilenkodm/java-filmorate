package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FeatureNotSupportedException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FilmInMemoryStorage extends BaseItemInMemoryStorage<FilmIdType, Film> implements FilmStorage {
    private final Map<FilmIdType, Set<UserIdType>> likes = new TreeMap<>();
    private FilmIdType lastId = FilmIdType.of(0L);

    @Override
    protected FilmIdType newItemId() {
        lastId = FilmIdType.of(lastId.getValue() + 1);
        return lastId;
    }

    @Override
    protected String idNotFoundMsg(FilmIdType id) {
        return String.format("Не найден фильм с кодом %s!", id);
    }


    @Override
    public Film createItem(Film item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), item);
        Film result = super.createItem(item);
        likes.put(result.getId(), new TreeSet<>());
        return result;
    }

    @Override
    public void deleteItem(FilmIdType id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), id);
        likes.remove(id);
        super.deleteItem(id);
    }

    @Override
    public int getLikesCount(FilmIdType id) {
        Set<UserIdType> users = likes.get(id);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(id), this.getClass(), log);
        }
        int result = users.size();
        log.info("Выполнено {}.getLikeCount({})", this.getClass().getName(), id);
        return result;
    }

    @Override
    public List<Film> getPopular(int maxCount, GenreIdType genreId, Integer year) {
        List<Film> result = items.keySet()
                .stream()
                .sorted(Comparator.comparing(this::getLikesCount).reversed().thenComparing(FilmIdType::getValue))
                .limit(maxCount)
                .map(items::get)
                .collect(Collectors.toList());
        log.info("Выполнено {}.getLikeCount({})", this.getClass().getName(), maxCount);
        return result;
    }

    @Override
    public void addLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> users = likes.get(filmId);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(filmId), this.getClass(), log);
        }
        users.add(userId);
        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), filmId, userId);
    }

    @Override
    public void removeLike(FilmIdType filmId, UserIdType userId) {
        Set<UserIdType> users = likes.get(filmId);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(filmId), this.getClass(), log);
        }
        if (!users.contains(userId)) {
            throw new KeyNotFoundException(String.format("Лайк пользователя %s не найден!", userId), this.getClass(), log);
        }
        users.remove(userId);
        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), filmId, userId);
    }

    @Override
    public List<Film> getFilmsByDirector(DirectorIdType directorId, FilmsByDirectorSortByMode sortBy) {
        throw new FeatureNotSupportedException(this.getClass(), log);
    }

    @Override
    public List<Film> getSearchedFilms(String query, Set<String> by) {
        throw new FeatureNotSupportedException(this.getClass(), log);
    }

    @Override
    public List<Film> getRecommendations(UserIdType userId) {
        throw new FeatureNotSupportedException(this.getClass(), log);
    }

    @Override
    public List<Film> getCommonFilms(UserIdType userId, UserIdType friendId) {
        throw new FeatureNotSupportedException(this.getClass(), log);
    }
}
