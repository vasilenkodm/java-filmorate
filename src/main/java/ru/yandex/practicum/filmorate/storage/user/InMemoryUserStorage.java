package ru.yandex.practicum.filmorate.storage.user;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<UserIdType, User> users;

    private final Map<UserIdType, Set<UserIdType>> friends;

    public InMemoryUserStorage() {
        users = new TreeMap<>();
        friends =  new TreeMap<>();
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean notExits(UserIdType userId) {
        return !users.containsKey(userId);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void updateUser(User user) {
        users.replace(user.getId(), user);
    }

    public User getUser(UserIdType userId) {
        return users.get(userId);
    }
    
    public void addFriend(UserIdType userId, UserIdType friendId) {
        friends.computeIfAbsent(userId, k -> new TreeSet<>()).add(friendId);
    }

    public void deleteFriend(UserIdType userId, UserIdType friendId) {
        Set<UserIdType> friendsSet = friends.get(userId);

        if (friendsSet!=null) {
            friendsSet.remove(friendId);
        }
    }

    public Set<User> friendsList(UserIdType userId) {
        Set<User> result = new LinkedHashSet<>();
        if ( friends.containsKey(userId)) {
            friends.get(userId).forEach(id -> result.add(users.get(id)));
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
