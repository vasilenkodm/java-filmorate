package ru.yandex.practicum.filmorate.storage.user;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<UserIdType, User> users = new TreeMap<>();;

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
        users.get(user.getId()).updateWith(user);
    }

    public User getUser(UserIdType userId) {
        return users.get(userId);
    }
    
}
