package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.type.UserIdType;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter @Getter @Builder @ToString @EqualsAndHashCode
public class User implements BaseItem<UserIdType, User> {
    @Getter @Setter
    private UserIdType id; //целочисленный идентификатор — id;

    @Getter @Setter @NotBlank(message = "Укажите электронную почту") @Email(message = "Нужен корректный адрес электронной почты!")
    private String email; //электронная почта — email;

    @Getter @Setter @NotBlank(message = "Логин не может быть пустым!") @Pattern(regexp = "\\S*$", message = "Логин не может содержать пробелы!")
    private String login; //логин пользователя — login;

    @Setter
    private String name; //имя для отображения — name;

    @Getter @Setter @NotNull @Past(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday; // дата рождения — birthday.

    @SuppressWarnings("unused")
    public String getName() {
        return (name==null || name.isBlank()) ? login : name;
    }

    public void updateWith(User user) {
        this.email = user.email;
        this.login = user.login;
        this.name = user.name;
        this.birthday = user.birthday;
    }

}
