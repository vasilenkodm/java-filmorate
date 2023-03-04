![Схема БД](https://github.com/vasilenkodm/java-filmorate/raw/main/Filmorate_database_schema.png)

Ссылка на схему: https://dbdiagram.io/d/63f28c35296d97641d821eec

//Filmorate
Table "UserInfo" {
  "user_id" BIGINT [pk, not null, increment, note: 'Код пользователя']
  "email" VARCHAR_IGNORECASE(320) [unique, not null, note: 'Электронная почта пользователя']
  "login" VARCHAR_IGNORECASE(100) [unique, not null, note: 'Логин пользователя']
  "user_name" VARCHAR(100) [not null, default: `''`, note: 'Имя пользователя для отображения']
  "birthday" DATE [not null, note: 'Дата родения пользователя']
  Note: 'Данные пользователей'
}

Table "Friendship" {
  "proposer_id" BIGINT [note: 'Предложивший дружбу пользователь']
  "invited_id" BIGINT [note: 'Приглашаемый дружить пользователь']

Indexes {
  (proposer_id, invited_id) [pk]
}
  Note: 'Дрежественные связи пользователей. Одна запись (А,Б) - протянутая рука, две записи (А,Б и Б,А) - подтвержденная дружба'
}

Table "Genre" {
  "genre_id" INT [pk, not null, increment, note: 'Код жанра']
  "genre_name" VARCHAR_IGNORECASE(100) [unique, not null, note: 'Название жанра']
  Note: 'Жанры фильмов'
}

Table "RankMPA" {
  "RankMPA_id" int [pk, not null, increment, note: 'Код рейтинга']
  "RankMPA_name" VARCHAR_IGNORECASE(10) [unique, note: 'Название (шифр) рейтинга']
  Note: 'Рейтинг MPA'
}

Table "Film" {
  "film_id" BIGINT [pk, not null, increment, note: 'Код фильма']
  "film_name" VARCHAR(255) [not null, note: 'Название фильма']
  "description" VARCHAR(200) [not null, note: 'Описание фильма']
  "release_date" date [not null, note: 'Дата релиза']
  "duration" int [not null, note: 'Продолжительность в секундах']
  "RankMPA_id" int [not null, note: 'Код рейтинга MPA']
  Note: 'Данные фильмов'
}

Table "FilmLikes" {
  "film_id" BIGINT [note: 'Код фильма']
  "user_id" BIGINT [note: 'Код пользователя']

Indexes {
  (film_id, user_id) [pk]
}
  Note: 'Фильмы, понравившиеся пользователям'
}

Table "FilmGenre" {
  "film_id" BIGINT [note: 'Код фильма']
  "genre_id" INT [note: 'Код жанра']

Indexes {
  (film_id, genre_id) [pk]
}
  Note: 'Жанры, к котрым относится фильм'
}

Ref "proposer_id_UserInfo_user_id":"UserInfo"."user_id" < "Friendship"."proposer_id" [update: cascade, delete: restrict]

Ref "invited_id_UserInfo_user_id":"UserInfo"."user_id" < "Friendship"."invited_id" [update: cascade, delete: restrict]

Ref "RankMPA_id_RankMPA_RankMPA_id":"RankMPA"."RankMPA_id" < "Film"."RankMPA_id" [update: cascade, delete: restrict]

Ref "FilmLikes_film_id_Film_film_id":"Film"."film_id" < "FilmLikes"."film_id" [update: cascade, delete: restrict]

Ref "FilmLikes_user_id_UserInfo_user_id":"UserInfo"."user_id" < "FilmLikes"."user_id" [update: cascade, delete: restrict]

Ref "FilmGenre_film_id_Film_film_id":"Film"."film_id" < "FilmGenre"."film_id" [update: cascade, delete: restrict]

Ref "FilmGenre_user_id_UserInfo_user_id":"Genre"."genre_id" < "FilmGenre"."genre_id" [update: cascade, delete: restrict]
