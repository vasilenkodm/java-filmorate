package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getUsers();
    boolean notExits(UserIdType userId);
    void addUser(User user);
    void updateUser(User user);
    User getUser(UserIdType userId);
    void addFriend(UserIdType userId, UserIdType friendId);
    void deleteFriend(UserIdType userId, UserIdType friendId);
    Set<User> friendsList(UserIdType userId);
    Set<User> commonFriends(UserIdType userId, UserIdType otherId);

}
