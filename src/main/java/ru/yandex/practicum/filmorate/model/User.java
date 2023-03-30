package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@ToString
@EqualsAndHashCode(callSuper = true)
public class User extends BaseItem<UserIdType, User> implements Item<UserIdType, User> {
    public User() {
        super();
    }

    @NotBlank(message = "Укажите электронную почту")
    @Email(message = "Нужен корректный адрес электронной почты!")
    private String email; //электронная почта — email;

    @NotBlank(message = "Логин не может быть пустым!")
    @Pattern(regexp = "\\S*$", message = "Логин не может содержать пробелы!")
    private String login; //логин пользователя — login;

    private String name; //имя для отображения — name;

    @NotNull
    @Past(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday; // дата рождения — birthday.

    @SuppressWarnings("unused")
    public String getName() {
        return (name==null || name.isBlank()) ? login : name;
    }

    @Override
    public void updateWith(User item) {
        this.email = item.email;
        this.login = item.login;
        this.name = item.name;
        this.birthday = item.birthday;
    }

}
