![Схема БД](https://github.com/vasilenkodm/java-filmorate/raw/main/Filmorate_database_schema.png)

https://dbdiagram.io/d/63f28c35296d97641d821eec

//Filmorate
Table UserInfo {
  user_id bigint [pk, not null, increment]
  email varchar(320) [not null, unique]
  login varchar(100) [not null, unique]
  user_name  varchar(100) 
  birth_day date [not null]
}

Table Friends {
  proposer_id bigint [PK, ref: > UserInfo.user_id]
  invited_id bigint [PK, ref: > UserInfo.user_id]
}

Table Genre {
  genre_id int [pk, not null, increment]
  genre_name varchar(100) [not null, unique]
}

Table RatingMPA {
  ratingMPA_id int [pk, not null]
  rating_name varchar(10) [unique]
  descriprion varchar(200) [not null]
  order int [not null, unique]
}

Table Film {
  film_id bigint [pk, not null, increment]
  film_name varchar(250) [not null]
  descriprion varchar(2000) [not null]
  release_date date [not null]
  duration int [not null, default:1]
  ratingMPA_id int [not null, ref: > RatingMPA.ratingMPA_id]
}

Table FilmLikers {
  film_id bigint [pk, ref: > Film.film_id]
  user_id bigint [pk, ref: > UserInfo.user_id]
}

Table FilmGenre {
  film_id bigint [pk, ref: > Film.film_id]
  genre_id int [pk, ref: > Genre.genre_id]
}
