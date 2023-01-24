package ru.yandex.practicum.filmorate.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@RestController
@RequestMapping("/users")
public class UserController extends CommonController<User> {

    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$" ;

    public UserController() {
        log = LoggerFactory.getLogger(UserController.class);
    }

    public void validate(User user) throws ValidationException {
        if (user.getEmail().isBlank()) {
            throw new ValidationException("Электронная почта не может быть пустой!");
        }

        if (!user.getEmail().matches(EMAIL_PATTERN)) {
            throw new ValidationException("Электронная почта не соответствует RFC 5322!");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        }

        if (user.getBirthday().compareTo(LocalDate.now()) > 0 ) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

}