package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class UserService  extends BaseItemService<UserIdType, User, UserStorage> {

    public UserService(UserStorage _storage) {
        super(_storage);
    }


    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    public void addFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.addFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        storage.addFriend(_userId, _friendId);
        //storage.addFriend(_friendId, _userId);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.deleteFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        storage.removeFriend(_userId, _friendId);
        //storage.removeFriend(_friendId, _userId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> friendsList(UserIdType _userId) {
        log.debug("Вызов {}.friendsList({})", this.getClass().getName(), _userId);
        return storage.getFriends(_userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> commonFriends(UserIdType _userId1, UserIdType _userId2) {
        log.debug("Вызов {}.commonFriends({}, {})", this.getClass().getName(), _userId1, _userId2);
        return storage.commonFriends(_userId1, _userId2);
    }

}
