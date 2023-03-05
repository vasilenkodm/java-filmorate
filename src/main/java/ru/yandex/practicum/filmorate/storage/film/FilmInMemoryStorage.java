package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

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
    protected String idNotFoundMsg(FilmIdType _id) {
        return String.format("Не найден фильм с кодом %s!", _id);
    }


    @Override
    public Film createItem(Film _item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), _item);
        Film result = super.createItem(_item);
        likes.put(result.getId(), new TreeSet<>());
        return result;
    }

    @Override
    public void deleteItem(FilmIdType _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        likes.remove(_id);
        super.deleteItem(_id);
    }

    @Override
    public int getLikesCount(FilmIdType _id) {
        Set<UserIdType> users = likes.get(_id);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_id), this.getClass(), log);
        }
        int result = users.size();
        log.info("Выполнено {}.getLikeCount({})", this.getClass().getName(), _id);
        return  result;
    }

    @Override
    public List<Film> getPopular(int _maxCount) {
        List<Film> result = items.keySet()
                .stream()
                .sorted(Comparator.comparing(this::getLikesCount).reversed().thenComparing(FilmIdType::getValue))
                .limit(_maxCount)
                .map(items::get)
                    .collect(Collectors.toList());
        log.info("Выполнено {}.getLikeCount({})", this.getClass().getName(), _maxCount);
        return result;
    }

    @Override
    public void addLike(FilmIdType _filmId, UserIdType _userId) {
        Set<UserIdType> users = likes.get(_filmId);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_filmId), this.getClass(), log);
        }
        users.add(_userId);
        log.info("Выполнено {}.addLike({}, {})", this.getClass().getName(), _filmId, _userId);
    }

    @Override
    public void removeLike(FilmIdType _filmId, UserIdType _userId) {
        Set<UserIdType> users = likes.get(_filmId);
        if (users == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_filmId), this.getClass(), log);
        }
        if (!users.contains(_userId)) {
            throw new KeyNotFoundException(String.format("Лайк пользователя %s не найден!", _userId), this.getClass(), log);
        }
        users.remove(_userId);
        log.info("Выполнено {}.removeLike({}, {})", this.getClass().getName(), _filmId, _userId);
    }

}
