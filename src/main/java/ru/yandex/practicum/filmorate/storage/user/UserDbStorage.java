package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Primary
public class UserDbStorage {/*extends BaseItemDbStorage<UserIdType, User> implements UserStorage {
    UserDbStorage(UserDAO dao) { super(dao); }

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
    */
}
