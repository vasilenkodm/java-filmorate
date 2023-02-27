package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseItemStorage;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.Set;

public interface UserStorage extends BaseItemStorage<UserIdType, User> {

    Set<UserIdType> getFriendsIds(UserIdType _userId);
    void addFriend(UserIdType userId, UserIdType _friendId);

    void removeFriend(UserIdType userId, UserIdType _friendId);

}
