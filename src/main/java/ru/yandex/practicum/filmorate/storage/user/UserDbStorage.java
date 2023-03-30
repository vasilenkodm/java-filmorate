package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseItemDbStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@Component
@Primary
public class UserDbStorage extends BaseItemDbStorage<UserIdType, User, UserDAO> implements UserStorage {
    UserDbStorage(UserDAO _dao) { super(_dao); }

    @Override
    public List<User> getFriends(UserIdType _userId) {
        log.debug("Вызов {}.getFriends({})", this.getClass().getName(), _userId);
        if (dao.notExists(_userId)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(_userId), this.getClass(), log);
        }
        return dao.getFriends(_userId);
    }

    @Override
    public List<User> commonFriends(UserIdType _userIdOne, UserIdType _userIdTwo) {
        log.debug("Вызов {}.commonFriends({}, {})", this.getClass().getName(), _userIdOne, _userIdTwo);
        if (dao.notExists(_userIdOne)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(_userIdOne), this.getClass(), log);
        }
        if (dao.notExists(_userIdTwo)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(_userIdTwo), this.getClass(), log);
        }
        return dao.getCommonFriends(_userIdOne, _userIdTwo);
    }

    @Override
    public void addFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.addFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        dao.addFriend(_userId, _friendId);
    }

    @Override
    public void removeFriend(UserIdType _userId, UserIdType _friendId) {
        log.debug("Вызов {}.removeFriend({}, {})", this.getClass().getName(), _userId, _friendId);
        dao.removeFriend(_userId, _friendId);
    }
}
