package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

public interface UserStorage extends ItemStorage<UserIdType, User> {

    void addFriend(UserIdType userId, UserIdType friendId);

    void removeFriend(UserIdType userId, UserIdType friendId);

    List<User> getFriends(UserIdType userId);

    List<User> commonFriends(UserIdType userIdOne, UserIdType userIdTwo);

}
