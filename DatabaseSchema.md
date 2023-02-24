![Схема БД](https://github.com/vasilenkodm/java-filmorate/raw/main/Filmorate_database_schema.png)

https://dbdiagram.io/d/63f28c35296d97641d821eec

//Filmorate
Table UserInfo {
  user_id BIGINT [pk, not null, increment,
    note: 'Код пользователя']
  email VARCHAR_IGNORECASE(320) [not null, unique,
    note: 'Электронная почта пользователя']
  login VARCHAR_IGNORECASE(100) [not null, unique,
    note: 'Логин пользователя']
  user_name  VARCHAR(100) [not null, default:'',
    note: 'Имя пользователя для отображения']
  birthday DATE [not null,
    note: 'Дата родения пользователя']
  Note: 'Данные пользователей'
}

Table Friendship {
  proposer_id BIGINT [PK,
    note: 'Предложивший дружбу пользователь']
  invited_id BIGINT [PK,
    note: 'Приглашаемый дружить пользователь']
  Note: 'Дрежественные связи пользователей. Одна запись ("А,Б") - "протянутая рука", две записи ("А,Б" и "Б,А") - "подтвержденная дружба"'
}
Ref proposer_id_UserInfo_user_id: Friendship.proposer_id > UserInfo.user_id [delete:restrict, update: cascade]
Ref invited_id_UserInfo_user_id: Friendship.invited_id > UserInfo.user_id [delete:restrict, update: cascade]

Table Genre {
  genre_id INT [pk, not null, increment,
    note: 'Код жанра']
  genre_name VARCHAR_IGNORECASE(100) [not null, unique,
    note: 'Название жанра']
  Note: 'Жанры фильмов'
}

Table RankMPA {
  RankMPA_id int [pk, not null,
    note: 'Код рейтинга']
  RankMPA_name VARCHAR_IGNORECASE(10) [unique,
    note: 'Название (шифр) рейтинга']
  description VARCHAR(200) [not null,
    note: 'Описание рейтинга']
  Note: 'Рейтинг MPA'
}

Table Film {
  film_id BIGINT [pk, not null, increment,
    note: 'Код фильма']
  film_name VARCHAR(255) [not null, default: '',
    note: 'Название фильма']
  descriprion VARCHAR(2000) [not null, default: '',
    note: 'Описание фильма']
  release_date date [not null,
    note: 'Дата релиза']
  duration int [not null, default:1,
    note: 'Продолжительность в секундах']
  RankMPA_id int [not null,
    note: 'Код рейтинга MPA']
  Note: 'Данные фильмов'
}
Ref RankMPA_id_RankMPA_RankMPA_id: Film.RankMPA_id > RankMPA.RankMPA_id [delete:restrict, update: cascade]

Table FilmLikers {
  film_id BIGINT [pk,
    note: 'Код фильма']
  user_id BIGINT [pk,
    note: 'Код пользователя']
  Note: 'Фильмы, понравившиеся пользователям'
}
Ref film_id_Film_film_id: FilmLikers.film_id > Film.film_id [delete:restrict, update: cascade]
Ref user_id_UserInfo_user_id: FilmLikers.user_id > UserInfo.user_id [delete:restrict, update: cascade]

Table FilmGenre {
  film_id BIGINT [pk,
    note: 'Код фильма']
  genre_id INT [pk,
    note: 'Код жанра']
  Note: 'Жанры, к котрым относится фильм'
}
Ref film_id_Film_film_id: FilmGenre.film_id > Film.film_id [delete:restrict, update: cascade]
Ref user_id_UserInfo_user_id: FilmGenre.genre_id > Genre.genre_id [delete:restrict, update: cascade]
