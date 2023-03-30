CREATE TABLE IF NOT EXISTS UserInfo (
  user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  email VARCHAR_IGNORECASE(320) UNIQUE NOT NULL,
  login VARCHAR_IGNORECASE(100) UNIQUE NOT NULL,
  user_name VARCHAR(100) NOT NULL DEFAULT '',
  birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS Friendship (
  proposer_id BIGINT,
  invited_id BIGINT,
  PRIMARY KEY (proposer_id, invited_id)
);

CREATE TABLE IF NOT EXISTS Genre (
  genre_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  genre_name VARCHAR_IGNORECASE(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS RankMPA (
  RankMPA_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  RankMPA_name VARCHAR_IGNORECASE(10) UNIQUE
);

CREATE TABLE IF NOT EXISTS Film (
  film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  film_name VARCHAR(255) NOT NULL,
  description VARCHAR(200) NOT NULL,
  release_date date NOT NULL,
  duration INT NOT NULL,
  RankMPA_id INT NOT NULL
);

CREATE TABLE IF NOT EXISTS FilmLikes (
  film_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS FilmGenre (
  film_id BIGINT,
  genre_id INT,
  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS Director (
  director_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  director_name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS FilmDirector (
  film_id BIGINT NOT NULL,
  director_id INT NOT NULL,
  PRIMARY KEY (film_id, director_id)
);

CREATE TABLE IF NOT EXISTS Review(
  review_id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  content VARCHAR(200) NOT NULL,
  is_positive boolean NOT NULL,
  user_id BIGINT NOT NULL,
  film_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS ReviewLikes(
  review_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  is_like boolean NOT NULL DEFAULT 0,
  PRIMARY KEY(review_id, user_id)
);

--CREATE INDEX ON FilmDirector (director_id);


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

COMMENT ON TABLE Director IS 'Режиссеры';

COMMENT ON COLUMN Director.director_id IS 'Код режиссера';

COMMENT ON COLUMN Director.director_name IS 'Имя режиссера';

COMMENT ON TABLE FilmDirector IS 'Режиссеры фильма';

COMMENT ON COLUMN FilmDirector.film_id IS 'Код фильма';

COMMENT ON COLUMN FilmDirector.director_id IS 'Код режиссера';

COMMENT ON TABLE Review IS 'Отзывы';

COMMENT ON COLUMN Review.review_id IS 'Код отзыва';

COMMENT ON COLUMN Review.content IS 'Текст отзыва';

COMMENT ON COLUMN Review.is_positive IS 'Тип отзыва';

COMMENT ON COLUMN Review.user_id IS 'Код пользователя';

COMMENT ON COLUMN Review.film_id IS 'Код фильма';

ALTER TABLE Film DROP CONSTRAINT IF EXISTS RankMPA_id_RankMPA_RankMPA_id;
ALTER TABLE Film ADD CONSTRAINT RankMPA_id_RankMPA_RankMPA_id FOREIGN KEY (RankMPA_id) REFERENCES RankMPA (RankMPA_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmDirector DROP CONSTRAINT IF EXISTS FilmDirector_director_id_Director_director_id;
ALTER TABLE FilmDirector ADD CONSTRAINT FilmDirector_director_id_Director_director_id FOREIGN KEY (director_id) REFERENCES Director (director_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmDirector DROP CONSTRAINT IF EXISTS FilmDirector_film_id_Film_film_id;
ALTER TABLE FilmDirector ADD CONSTRAINT FilmDirector_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmGenre DROP CONSTRAINT IF EXISTS FilmGenre_film_id_Film_film_id;
ALTER TABLE FilmGenre ADD CONSTRAINT FilmGenre_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmGenre DROP CONSTRAINT IF EXISTS FilmGenre_user_id_UserInfo_user_id;
ALTER TABLE FilmGenre ADD CONSTRAINT FilmGenre_user_id_UserInfo_user_id FOREIGN KEY (genre_id) REFERENCES Genre (genre_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmLikes DROP CONSTRAINT IF EXISTS FilmLikes_film_id_Film_film_id;
ALTER TABLE FilmLikes ADD CONSTRAINT FilmLikes_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE FilmLikes DROP CONSTRAINT IF EXISTS FilmLikes_user_id_UserInfo_user_id;
ALTER TABLE FilmLikes ADD CONSTRAINT FilmLikes_user_id_UserInfo_user_id FOREIGN KEY (user_id) REFERENCES UserInfo (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Friendship DROP CONSTRAINT IF EXISTS FRIENDSHIP_CHECK;
ALTER TABLE Friendship ADD CONSTRAINT FRIENDSHIP_CHECK CHECK (proposer_id<>invited_id);

ALTER TABLE Friendship DROP CONSTRAINT IF EXISTS invited_id_UserInfo_user_id;
ALTER TABLE Friendship ADD CONSTRAINT invited_id_UserInfo_user_id FOREIGN KEY (invited_id) REFERENCES UserInfo (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Friendship DROP CONSTRAINT IF EXISTS proposer_id_UserInfo_user_id;
ALTER TABLE Friendship ADD CONSTRAINT proposer_id_UserInfo_user_id FOREIGN KEY (proposer_id) REFERENCES UserInfo (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Review DROP CONSTRAINT IF EXISTS Review_film_id_Film_film_id;
ALTER TABLE Review ADD CONSTRAINT Review_film_id_Film_film_id FOREIGN KEY (film_id) REFERENCES Film (film_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Review DROP CONSTRAINT IF EXISTS Review_user_id_UserInfo_user_id;
ALTER TABLE Review ADD CONSTRAINT Review_user_id_UserInfo_user_id FOREIGN KEY (user_id) REFERENCES UserInfo (user_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE ReviewLikes DROP CONSTRAINT IF EXISTS ReviewLikes_review_id_Review_review_id;
ALTER TABLE ReviewLikes ADD CONSTRAINT ReviewLikes_review_id_Review_review_id FOREIGN KEY (review_id) REFERENCES Review (review_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE ReviewLikes DROP CONSTRAINT IF EXISTS ReviewLikes_user_id_UserInfo_user_id;
ALTER TABLE ReviewLikes ADD CONSTRAINT ReviewLikes_user_id_UserInfo_user_id FOREIGN KEY (user_id) REFERENCES UserInfo (user_id) ON DELETE CASCADE ON UPDATE CASCADE;