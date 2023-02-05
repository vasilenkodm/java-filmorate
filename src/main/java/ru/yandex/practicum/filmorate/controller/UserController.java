package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping()
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("Добавление {}", user);
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("Обновление {}", user);
        return service.update(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable UserIdType userId) {
        User user = service.get(userId);
        log.info("Получение по ключу {} {}", userId, user);
        return user;
    }
    
    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    @PutMapping("/{userId}/friends/{friendId}")
    public void friendsAdd(@PathVariable UserIdType userId, @PathVariable UserIdType friendId) {
        service.addFriend(userId, friendId);
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void friendsDel(@PathVariable UserIdType userId, @PathVariable UserIdType friendId) {
        service.deleteFriend(userId, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/{userId}/friends")
    public Set<User> friendsList(@PathVariable UserIdType userId) {
        return service.friendsList(userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("/{userId}/friends/common/{otherId}")
    public Set<User> commonFriends(@PathVariable UserIdType userId, @PathVariable UserIdType otherId) {
        return service.commonFriends(userId, otherId);
    }
}