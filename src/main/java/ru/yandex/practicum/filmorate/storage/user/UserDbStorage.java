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
    UserDbStorage(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public List<User> getFriends(UserIdType userId) {
        log.debug("Вызов {}.getFriends({})", this.getClass().getName(), userId);
        if (dao.notExists(userId)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(userId), this.getClass(), log);
        }
        return dao.getFriends(userId);
    }

    @Override
    public List<User> commonFriends(UserIdType userIdOne, UserIdType userIdTwo) {
        log.debug("Вызов {}.commonFriends({}, {})", this.getClass().getName(), userIdOne, userIdTwo);
        if (dao.notExists(userIdOne)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(userIdOne), this.getClass(), log);
        }
        if (dao.notExists(userIdTwo)) {
            throw new KeyNotFoundException(dao.idNotFoundMsg(userIdTwo), this.getClass(), log);
        }
        return dao.getCommonFriends(userIdOne, userIdTwo);
    }

    @Override
    public void addFriend(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.addFriend({}, {})", this.getClass().getName(), userId, friendId);
        dao.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(UserIdType userId, UserIdType friendId) {
        log.debug("Вызов {}.removeFriend({}, {})", this.getClass().getName(), userId, friendId);
        dao.removeFriend(userId, friendId);
    }
}
