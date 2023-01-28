package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User implements Item {
    @Getter @Setter
    private int id; //целочисленный идентификатор — id;

    //@Getter @Setter @Email @NotBlank(message = "Электронная почта не может быть пустой!")
    @Getter @Setter @NotBlank(message = "Укажите электронную почту") @Email(message = "Нужен корректный адрес электронной почты!")
    private String email; //электронная почта — email;

    @Getter @Setter @NotBlank(message = "Логин не может быть пустым!") @Pattern(regexp = "\\S*$", message = "Логин не может содержать пробелы!")
    private String login; //логин пользователя — login;

    @Setter
    private String name; //имя для отображения — name;

    @Getter @Setter @NotNull @Past(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday; // дата рождения — birthday.

    public String getName() {
        return (name==null || name.isBlank()) ? login : name;
    }

}
