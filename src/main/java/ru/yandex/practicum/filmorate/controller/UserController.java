package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController  extends BaseItemController<UserIdType, User, UserService> {
    public UserController(UserService _service) {
        super(_service);
    }

    //PUT /users/{id}/friends/{friendId}  — добавление в друзья.
    @SuppressWarnings("unused")
    @PutMapping("/{userId}/friends/{friendId}")
    public void friendsAdd(@PathVariable UserIdType userId, @PathVariable UserIdType friendId) {
        service.addFriend(userId, friendId);
    }

    //DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    @SuppressWarnings("unused")
    @DeleteMapping("/{userId}/friends/{friendId}")
    public void friendsDel(@PathVariable UserIdType userId, @PathVariable UserIdType friendId) {
        service.deleteFriend(userId, friendId);
    }

    //GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    @SuppressWarnings("unused")
    @GetMapping("/{userId}/friends")
    public List<User> friendsList(@PathVariable UserIdType userId) {
        return service.friendsList(userId);
    }

    //GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @SuppressWarnings("unused")
    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable UserIdType userId, @PathVariable UserIdType otherId) {
        return service.commonFriends(userId, otherId);
    }
}
