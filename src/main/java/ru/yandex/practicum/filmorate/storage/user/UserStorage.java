package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ItemStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

public interface UserStorage extends ItemStorage<UserIdType, User> {

    void addFriend(UserIdType _userId, UserIdType _friendId);

    void removeFriend(UserIdType _userId, UserIdType _friendId);

    List<User> getFriends(UserIdType _userId);

    List<User> commonFriends(UserIdType _userIdOne, UserIdType _userIdTwo);

}
