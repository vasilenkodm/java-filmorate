package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Slf4j
@Component
@Primary
public class InMemoryUserStorage extends BaseItemInMemoryStorage<UserIdType, User> implements UserStorage {

    private Map<UserIdType, Set<UserIdType>> friends =  new TreeMap<>();

    @Override
    protected String idNotFoundMsg(UserIdType _id) {
        return String.format("Не найден пользователь с кодом %s!", _id);
    }

    @Override
    public User createItem(User _item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), _item);
        friends.put(_item.getId(), new TreeSet<>());
        return super.createItem(_item);
    }

    @Override
    public void deleteItem(UserIdType _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        friends.remove(_id);
        super.deleteItem(_id);
    }

    @Override
    public Set<UserIdType> getFriendsIds(UserIdType _userId) {
        Set<UserIdType> result = friends.get(_userId);

        if (result == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }

        log.info("Выполнено {}.getFriendsIds({})", this.getClass().getName(), _userId);
        return result;
    }
    
    public void addFriend(UserIdType _userId, UserIdType _friendId) {
        Set<UserIdType> set = friends.get(_userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }
        set.add(_friendId);
        log.info("Выполнено {}.addFriend({}, {})", this.getClass().getName(), _userId, _friendId);
    }

    public void removeFriend(UserIdType _userId, UserIdType _friendId) {
        Set<UserIdType> set = friends.get(_userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }
        set.remove(_friendId);
        log.info("Выполнено {}.removeFriend({}, {})", this.getClass().getName(), _userId, _friendId);
    }

}
