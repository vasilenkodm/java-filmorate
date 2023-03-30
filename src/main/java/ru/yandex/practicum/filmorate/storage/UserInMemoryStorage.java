package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
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
    protected String idNotFoundMsg(UserIdType id) {
        return String.format("Не найден пользователь с кодом %s!", id);
    }

    @Override
    public User createItem(User item) {
        log.debug("Вызов {}.createItem({})", this.getClass().getName(), item);
        User result = super.createItem(item);
        friends.put(result.getId(), new TreeSet<>());
        return result;
    }

    @Override
    public void deleteItem(UserIdType id) {
        log.debug("Вызов {}.deleteItem({})", this.getClass().getName(), id);
        friends.remove(id);
        super.deleteItem(id);
    }

    @Override
    public void addFriend(UserIdType userId, UserIdType friendId) {
        Set<UserIdType> set = friends.get(userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(userId), this.getClass(), log);
        }
        if (!items.containsKey(friendId)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(friendId), this.getClass(), log);
        }

        set.add(friendId);
        log.info("Выполнено {}.addFriend({}, {})", this.getClass().getName(), userId, friendId);
    }

    @Override
    public void removeFriend(UserIdType userId, UserIdType friendId) {
        Set<UserIdType> set = friends.get(userId);

        if (set == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(userId), this.getClass(), log);
        }
        if (!items.containsKey(friendId)) {
            throw new KeyNotFoundException(this.idNotFoundMsg(friendId), this.getClass(), log);
        }
        set.remove(friendId);
        log.info("Выполнено {}.removeFriend({}, {})", this.getClass().getName(), userId, friendId);
    }

    @Override
    public List<User> getFriends(UserIdType userId) {
        Set<UserIdType> friendsIds = friends.get(userId);

        if (friendsIds == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(userId), this.getClass(), log);
        }
        List<User> result = friendsIds.stream().map(items::get).collect(Collectors.toList());
        log.info("Выполнено {}.getFriends({})", this.getClass().getName(), userId);
        return result;
    }

    @Override
    public List<User> commonFriends(UserIdType userIdOne, UserIdType userIdTwo) {
        Set<UserIdType> friendsIds1 = friends.get(userIdOne);
        Set<UserIdType> friendsIds2 = friends.get(userIdTwo);

        if (friendsIds1 == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(userIdOne), this.getClass(), log);
        }
        if (friendsIds2 == null) {
            throw new KeyNotFoundException(this.idNotFoundMsg(userIdTwo), this.getClass(), log);
        }

        Set<UserIdType> commonFriendsIds = new HashSet<>(friendsIds1);
        commonFriendsIds.retainAll(friendsIds2);
        List<User> result = commonFriendsIds.stream().map(items::get).collect(Collectors.toList());
        log.info("Выполнено {}.commonFriends({}, {})", this.getClass().getName(), userIdOne, userIdTwo);
        return result;
    }

}
