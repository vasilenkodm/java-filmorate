
https://dbdiagram.io/d/63f28c35296d97641d821eec

//Filmorate
Table User {
  id bigint [pk, not null, increment]
  email varchar(320) [not null, unique]
  login varchar(100) [not null, unique]
  name  varchar(100) 
  birth_day date [not null]
}

Table Fiends {
  proposer_id bigint [PK, ref: > User.id]
  invited_id bigint [PK, ref: > User.id]
}

Table Genre {
  id int [pk, not null, increment]
  name varchar(100) [not null, unique]
}

Table RatingMPA {
  id varchar(10) [pk, not null]
  descriprion varchar(200) [not null]
  order int [not null, unique]
}

Table Film {
  id bigint [pk, not null, increment]
  name varchar(250) [not null]
  descriprion varchar(2000) [not null]
  release_date date [not null]
  duration int [not null, default:1]
  ratingMPA_id varchar(10) [not null, ref: > RatingMPA.id]
}

Table FilmLikes {
  film_id bigint [pk, ref: > Film.id]
  user_id bigint [pk, ref: > User.id]
}

Table FilmGenre {
  film_id bigint [pk, ref: > Film.id]
  genre_id int [pk, ref: > Genre.id]
}
