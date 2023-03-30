package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.type.EventType;
import ru.yandex.practicum.filmorate.type.OperationType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Service
public class UserService extends BaseItemService<UserIdType, User, UserStorage> {

    private final EventStorage eventStorage;

    public UserService(UserStorage _storage, EventStorage eventStorage) {
        super(_storage);
        this.eventStorage = eventStorage;
    }


    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    public void addFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.addFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        storage.addFriend(_userId, _friendId);
        eventStorage.addEvent(_userId.getValue(), _friendId.getValue(), EventType.FRIEND, OperationType.ADD);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.deleteFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        storage.removeFriend(_userId, _friendId);
        eventStorage.addEvent(_userId.getValue(), _friendId.getValue(), EventType.FRIEND, OperationType.REMOVE);
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


    //GET /users/{id}/feed - список ленты событий
    public List<Event> getFeed(UserIdType _userId) {
        log.debug("Вызов {}.getFeed({})", this.getClass().getName(), _userId);
        return eventStorage.getFeedForUser(_userId);
    }
}