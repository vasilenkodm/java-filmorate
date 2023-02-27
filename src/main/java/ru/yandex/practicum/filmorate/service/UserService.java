package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    /*
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
        List<User> result = storage.getAllItems();
        return result;
    }

    public User create(final User user) {
        UserIdType key = getNewId();
        user.setId(key);
        storage.createItem(user);
        return user;
    }

    public User update(final User user) {
        UserIdType id = user.getId();
        if (storage.notExits(id)) {
            throw new KeyNotFoundException("Обновление: не найден ключ "+id+"! "+ user, this.getClass(), log);
        }
        storage.updateItem(user);
        log.info("Обновление {}", user);
        return user;
    }

    public User get(UserIdType key) {
        if (storage.notExits(key)) {
            throw new KeyNotFoundException("Получение: не найден ключ "+key+"!", this.getClass(), log);
        }
        return storage.readItem(key);
    }

    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    public void addFriend(UserIdType userId, UserIdType friendId) {
        User user = storage.readItem(userId);
        if (user == null) {
            throw new KeyNotFoundException("Добавление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        User friend = storage.readItem(friendId);
        if (friend == null) {
            throw new KeyNotFoundException("Добавление друга: не найден друг "+friendId, this.getClass(), log);
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
    }


    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        User user = storage.readItem(userId);
        if (user == null) {
            throw new KeyNotFoundException("Удаление друга: не найден пользователь "+userId, this.getClass(), log);
        }
        User friend = storage.readItem(friendId);
        if (friend == null) {
            throw new KeyNotFoundException("Удаление друга: не найден друг "+friendId, this.getClass(), log);
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public Set<User> friendsList(UserIdType userId) {
        User user = storage.readItem(userId);
        if (user == null) {
            throw new KeyNotFoundException("Список друзей: не найден пользователь "+userId, this.getClass(), log);
        }
        return user .getFriendsIds()
                    .stream()
                    .map(storage::readItem)
                    .collect(Collectors.toList());
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    public List<User> commonFriends(UserIdType userId, UserIdType friendId) {
        User user = storage.readItem(userId);
        if (user == null) {
            throw new KeyNotFoundException("Общие друзья: не найден пользователь "+userId, this.getClass(), log);
        }

        User friend = storage.readItem(friendId);
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
                            .map(storage::readItem)
                            .collect(Collectors.toList());
    }
    */
}
