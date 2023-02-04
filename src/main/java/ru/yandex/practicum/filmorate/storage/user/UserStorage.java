package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.Map;
import java.util.Set;

public interface UserStorage {
    Map<UserIdType, User> getUsers();

    void addFriend(UserIdType userId, UserIdType friendId);
    void deleteFriend(UserIdType userId, UserIdType friendId);
    Set<User> friendsList(UserIdType userId);
    Set<User> commonFriends(UserIdType userId, UserIdType otherId);

}
