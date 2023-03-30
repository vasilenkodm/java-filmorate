package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class UserService extends BaseItemService<UserIdType, User, UserStorage> {

    public UserService(UserStorage userStorage) {
        super(userStorage);
    }


    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    public void addFriend(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.addFriend({}, {})", this.getClass().getName(), userId, friendId);
        storage.addFriend(userId, friendId);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.deleteFriend({}, {})", this.getClass().getName(), userId, friendId);
        storage.removeFriend(userId, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> friendsList(UserIdType userId) {
        log.debug("Вызов {}.friendsList({})", this.getClass().getName(), userId);
        return storage.getFriends(userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> commonFriends(UserIdType userId1, UserIdType userId2) {
        log.debug("Вызов {}.commonFriends({}, {})", this.getClass().getName(), userId1, userId2);
        return storage.commonFriends(userId1, userId2);
    }

}
