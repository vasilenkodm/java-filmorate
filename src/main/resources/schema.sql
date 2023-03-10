CREATE TABLE UserInfo (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  email VARCHAR_IGNORECASE(320) UNIQUE NOT NULL,
  login VARCHAR_IGNORECASE(100) UNIQUE NOT NULL,
  user_name VARCHAR(100) NOT NULL DEFAULT '',
  birthday DATE NOT NULL
);

CREATE TABLE Friendship (
  proposer_id BIGINT,
  invited_id BIGINT,
  PRIMARY KEY (proposer_id, invited_id)
);

CREATE TABLE Genre (
  genre_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  genre_name VARCHAR_IGNORECASE(100) UNIQUE NOT NULL
);

CREATE TABLE RankMPA (
  RankMPA_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  RankMPA_name VARCHAR_IGNORECASE(10) UNIQUE
);

CREATE TABLE Film (
  film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  film_name VARCHAR(255) NOT NULL,
  description VARCHAR(200) NOT NULL,
  release_date date NOT NULL,
  duration int NOT NULL,
  RankMPA_id int NOT NULL
);

CREATE TABLE FilmLikes (
  film_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE FilmGenre (
  film_id BIGINT,
  genre_id INT,
  PRIMARY KEY (film_id, genre_id)
);

COMMENT ON TABLE UserInfo IS 'Данные пользователей';

COMMENT ON COLUMN UserInfo.user_id IS 'Код пользователя';

COMMENT ON COLUMN UserInfo.email IS 'Электронная почта пользователя';

COMMENT ON COLUMN UserInfo.login IS 'Логин пользователя';

COMMENT ON COLUMN UserInfo.user_name IS 'Имя пользователя для отображения';

COMMENT ON COLUMN UserInfo.birthday IS 'Дата родения пользователя';

COMMENT ON TABLE Friendship IS 'Дрежественные связи пользователей. Одна запись (А,Б) - протянутая рука, две записи (А,Б и Б,А) - подтвержденная дружба';

COMMENT ON COLUMN Friendship.proposer_id IS 'Предложивший дружбу пользователь';

COMMENT ON COLUMN Friendship.invited_id IS 'Приглашаемый дружить пользователь';

COMMENT ON TABLE Genre IS 'Жанры фильмов';

COMMENT ON COLUMN Genre.genre_id IS 'Код жанра';

COMMENT ON COLUMN Genre.genre_name IS 'Название жанра';

COMMENT ON TABLE RankMPA IS 'Рейтинг MPA';

COMMENT ON COLUMN RankMPA.RankMPA_id IS 'Код рейтинга';

COMMENT ON COLUMN RankMPA.RankMPA_name IS 'Название (шифр) рейтинга';

COMMENT ON TABLE Film IS 'Данные фильмов';

COMMENT ON COLUMN Film.film_id IS 'Код фильма';

COMMENT ON COLUMN Film.film_name IS 'Название фильма';

COMMENT ON COLUMN Film.description IS 'Описание фильма';

COMMENT ON COLUMN Film.release_date IS 'Дата релиза';

COMMENT ON COLUMN Film.duration IS 'Продолжительность в секундах';

COMMENT ON COLUMN Film.RankMPA_id IS 'Код рейтинга MPA';

COMMENT ON TABLE FilmLikes IS 'Фильмы, понравившиеся пользователям';

COMMENT ON COLUMN FilmLikes.film_id IS 'Код фильма';

COMMENT ON COLUMN FilmLikes.user_id IS 'Код пользователя';

COMMENT ON TABLE FilmGenre IS 'Жанры, к котрым относится фильм';

COMMENT ON COLUMN FilmGenre.film_id IS 'Код фильма';

COMMENT ON COLUMN FilmGenre.genre_id IS 'Код жанра';

ALTER TABLE Friendship ADD CONSTRAINT proposer_id_UserInfo_user_id FOREIGN KEY (proposer_id) REFERENCES UserInfo (user_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE Friendship ADD CONSTRAINT invited_id_UserInfo_user_id FOREIGN KEY (invited_id) REFERENCES UserInfo (user_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE Film ADD CONSTRAINT RankMPA_id_RankMPA_RankMPA_id FOREIGN KEY (RankMPA_id) REFERENCES RankMPA (RankMPA_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE FilmLikes ADD CONSTRAINT FilmLikes_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE FilmLikes ADD CONSTRAINT FilmLikes_user_id_UserInfo_user_id FOREIGN KEY (user_id) REFERENCES UserInfo (user_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE FilmGenre ADD CONSTRAINT FilmGenre_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE FilmGenre ADD CONSTRAINT FilmGenre_user_id_UserInfo_user_id FOREIGN KEY (genre_id) REFERENCES Genre (genre_id) ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE Friendship ADD CONSTRAINT FRIENDSHIP_CHECK CHECK (proposer_id<>invited_id);

