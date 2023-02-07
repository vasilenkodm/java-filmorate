package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();
    boolean notExits(UserIdType userId);
    void addUser(User user);
    void updateUser(User user);
    User getUser(UserIdType userId);
}
