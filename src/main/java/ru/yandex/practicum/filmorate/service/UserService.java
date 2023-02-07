package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    private UserIdType lastUserId;
    
    public UserService(UserStorage storage) {
        this.storage = storage;
        lastUserId =  new UserIdType(0L);
    }

    private synchronized UserIdType getNewId() {
        lastUserId =  new UserIdType(lastUserId.getValue()+1);
        return lastUserId;
    }

    public List<User> getAllUsers() {
        return storage.getUsers();
    }

    public User create(final User user) {
        UserIdType key = getNewId();
        user.setId(key);
        storage.addUser(user);
        return user;
    }

    public User update(final User user) {
        UserIdType id = user.getId();
        if (storage.notExits(id)) {
            throw new KeyNotFoundException("Обновление: не найден ключ "+id+"! "+ user, this.getClass(), log);
        }
        storage.updateUser(user);
        log.info("Обновление {}", user);
        return user;
    }

    public User get(UserIdType key) {
        if (storage.notExits(key)) {
            throw new KeyNotFoundException("Получение: не найден ключ "+key+"!", this.getClass(), log);
        }
        return storage.getUser(key);
    }

    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    public void addFriend(UserIdType userId, UserIdType friendId) {
        User user = storage.getUser(userId);
        if (user == null) {
            throw new KeyNotFoundException("Добавление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        User friend = storage.getUser(friendId);
        if (friend == null) {
            throw new KeyNotFoundException("Добавление друга: не найден друг "+friendId, this.getClass(), log);
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        User user = storage.getUser(userId);
        if (user == null) {
            throw new KeyNotFoundException("Удаление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        User friend = storage.getUser(friendId);
        if (friend == null) {
            throw new KeyNotFoundException("Удаление друга: не найден друг "+friendId, this.getClass(), log);
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> friendsList(UserIdType userId) {
        User user = storage.getUser(userId);
        if (user == null) {
            throw new KeyNotFoundException("Список друзей: не найден пользователь "+userId, this.getClass(), log);
        }
        return user .getFriendsIds()
                    .stream()
                    .collect( ArrayList::new,
                                (list, id) -> list.add(storage.getUser(id)),
                                ArrayList::addAll);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> commonFriends(UserIdType userId, UserIdType friendId) {
        User user = storage.getUser(userId);
        if (user == null) {
            throw new KeyNotFoundException("Общие друзья: не найден пользователь "+userId, this.getClass(), log);
        }

        User friend = storage.getUser(friendId);
        if (friend == null) {
            throw new KeyNotFoundException("Общие друзья: не найден друг "+friendId, this.getClass(), log);
        }

        List<UserIdType> firstIds = user.getFriendsIds();
        List<UserIdType> secondIds = friend.getFriendsIds();
        List<UserIdType> smallIdsList;
        List<UserIdType> bigIdsList;
        if (firstIds.size() < secondIds.size()) {
            smallIdsList = firstIds;
            bigIdsList = secondIds;
        } else {
            smallIdsList = secondIds;
            bigIdsList = firstIds;
        }

        return smallIdsList.stream()
                            .filter(bigIdsList::contains)
                            .collect(ArrayList::new, (list, id)->list.add(storage.getUser(id)), List::addAll);
    }

}
