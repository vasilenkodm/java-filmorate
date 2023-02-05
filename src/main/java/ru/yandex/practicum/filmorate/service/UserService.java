package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

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
        UserIdType key = user.getId();

        if (storage.notExits(key)) {
            throw new KeyNotFoundException("Обновление: не найден ключ "+key+"! "+ user, this.getClass(), log);
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
        if (storage.notExits(userId)) {
            throw new KeyNotFoundException("Добавление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        if (storage.notExits(friendId)) {
            throw new KeyNotFoundException("Добавление друга: не найден друг "+friendId, this.getClass(), log);
        }
        storage.addFriend(userId, friendId);
        storage.addFriend(friendId, userId);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        if (storage.notExits(userId)) {
            throw new KeyNotFoundException("Удаление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        if (storage.notExits(friendId)) {
            throw new KeyNotFoundException("Удаление друга: не найден друг "+friendId, this.getClass(), log);
        }
        storage.deleteFriend(userId, friendId);
        storage.deleteFriend(friendId, userId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public Set<User> friendsList(UserIdType userId) {
        return storage.friendsList(userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public Set<User> commonFriends(UserIdType userId, UserIdType otherId) {
        return storage.commonFriends(userId, otherId);
    }
}