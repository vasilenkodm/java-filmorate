package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseItemInMemoryStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserInMemoryStorage extends BaseItemInMemoryStorage<UserIdType, User> implements UserStorage {

    private final Map<UserIdType, Set<UserIdType>> friends = new TreeMap<>();
    private UserIdType lastId = UserIdType.of(0L);

    @Override
    protected UserIdType newItemId() {
        lastId = UserIdType.of(lastId.getValue() + 1);
        return lastId;
    }

    @Override
    protected String idNotFoundMsg(UserIdType _id) {
        return String.format("Не найден пользователь с кодом %s!", _id);
    }

    @Override
    public User createItem(User _item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), _item);
        User result = super.createItem(_item);
        friends.put(result.getId(), new TreeSet<>());
        return result;
    }

    @Override
    public void deleteItem(UserIdType _id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), _id);
        friends.remove(_id);
        super.deleteItem(_id);
    }

    @Override
    public void addFriend(UserIdType _userId, UserIdType _friendId) {
        Set<UserIdType> set = friends.get(_userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }
        if (!items.containsKey(_friendId)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_friendId), this.getClass(), log);
        }

        set.add(_friendId);
        log.info("Выполнено {}.addFriend({}, {})", this.getClass().getName(), _userId, _friendId);
    }

    @Override
    public void removeFriend(UserIdType _userId, UserIdType _friendId) {
        Set<UserIdType> set = friends.get(_userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }
        if (!items.containsKey(_friendId)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_friendId), this.getClass(), log);
        }
        set.remove(_friendId);
        log.info("Выполнено {}.removeFriend({}, {})", this.getClass().getName(), _userId, _friendId);
    }

    @Override
    public List<User> getFriends(UserIdType _userId) {
        Set<UserIdType> friendsIds = friends.get(_userId);

        if (friendsIds == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId), this.getClass(), log);
        }
        List<User> result = friendsIds.stream().map(items::get).collect(Collectors.toList());
        log.info("Выполнено {}.getFriends({})", this.getClass().getName(), _userId);
        return result;
    }

    @Override
    public List<User> commonFriends(UserIdType _userId1, UserIdType _userId2) {
        Set<UserIdType> friendsIds1 = friends.get(_userId1);
        Set<UserIdType> friendsIds2 = friends.get(_userId2);

        if (friendsIds1 == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId1), this.getClass(), log);
        }
        if (friendsIds2 == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(_userId2), this.getClass(), log);
        }

        Set<UserIdType> commonFriendsIds = new HashSet<>(friendsIds1);
        commonFriendsIds.retainAll(friendsIds2);
        List<User> result = commonFriendsIds.stream().map(items::get).collect(Collectors.toList());
        log.info("Выполнено {}.commonFriends({}, {})", this.getClass().getName(), _userId1, _userId2);
        return result;
    }

}
