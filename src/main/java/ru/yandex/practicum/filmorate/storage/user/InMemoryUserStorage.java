package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{
    @Getter
    private final Map<UserIdType, User> users;

    private final Map<UserIdType, Set<UserIdType>> friends;

    public InMemoryUserStorage() {
        users = new TreeMap<>();
        friends =  new TreeMap<>();
    }

    public void addFriend(UserIdType userId, UserIdType friendId) {
        Set<UserIdType> friendsSet = friends.get(userId);
        if (friendsSet==null) {
            friendsSet = new TreeSet<>();
            friends.put(userId, friendsSet);
        }
        friendsSet.add(friendId);
    }
    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        Set<UserIdType> friendsSet = friends.get(userId);

        if (friendsSet==null) {
            friendsSet.remove(friendId);
        }
    }

    public Set<User> friendsList(UserIdType userId) {
        Set<User> result = new LinkedHashSet<>();
        if ( friends.containsKey(userId)) {
            friends.get(userId).stream().forEach(id -> result.add(users.get(id)));
        }
        return result;
    }

    public Set<User> commonFriends(UserIdType userId, UserIdType otherId) {
        Set<User> result = new LinkedHashSet<>();
        if ( friends.containsKey(userId) && friends.containsKey(otherId)) {
            Set<UserIdType> tempSet = new HashSet<>(friends.get(userId));
            tempSet.retainAll(friends.get(otherId));
            tempSet.forEach(id -> result.add(users.get(id)));
        }
        return result;
    }

}
