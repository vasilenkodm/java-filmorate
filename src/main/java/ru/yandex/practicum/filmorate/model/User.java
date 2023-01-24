package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class User implements Item {
    private int id; //целочисленный идентификатор — id;

    private String email; //электронная почта — email;

    private String login; //логин пользователя — login;

    private String name; //имя для отображения — name;

    private LocalDate birthday; // дата рождения — birthday.

    public String getName() {
        return (name==null || name.isBlank()) ? login : name;
    }

}
