package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rankmpa.RankMPAStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.type.FilmIdType;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDbStorage genreStorage;
	private final RankMPAStorage mpaStorage;

	@Test
	@Order(1)
	void contextLoads() {
		assertEquals(1, 1);
	}

	@Test
	@Order(2)
	void testMPAStorage() {
		final String NAME1 = "+++++";
		final String NAME2 = "-----";
		List<RankMPA> mpaList = mpaStorage.getAllItems();
		int lastSize = mpaList.size();
		RankMPA item2Create = RankMPA.builder().name(NAME1).build();
		final RankMPA[] item = {mpaStorage.createItem(item2Create)};
		assertEquals(NAME1, item[0].getName());
		assertEquals(1, mpaStorage.getAllItems().size() - lastSize);
		assertThrows(DataAccessException.class, () -> mpaStorage.createItem(item2Create));
		item[0].setName(NAME2);
		assertDoesNotThrow(() -> item[0] = mpaStorage.updateItem(item[0]));
		assertEquals(1, mpaStorage.getAllItems().size() - lastSize);
		assertDoesNotThrow(() -> mpaStorage.deleteItem(item[0].getId()));
		assertEquals(lastSize, mpaStorage.getAllItems().size());
		assertThrows(KeyNotFoundException.class, () -> mpaStorage.deleteItem(item[0].getId()));
	}

	@Test
	@Order(3)
	void testGenreStorage() {
		final String NAME1 = "+++++";
		final String NAME2 = "-----";
		List<Genre> genreList = genreStorage.getAllItems();
		int lastSize = genreList.size();
		Genre item2Create = Genre.builder().name(NAME1).build();
		final Genre[] item = {genreStorage.createItem(item2Create)};
		assertEquals(NAME1, item[0].getName());
		assertEquals(1, genreStorage.getAllItems().size() - lastSize);
		assertThrows(DataAccessException.class, () -> genreStorage.createItem(item2Create));
		item[0].setName(NAME2);
		assertDoesNotThrow(() -> item[0] = genreStorage.updateItem(item[0]));
		assertEquals(1, genreStorage.getAllItems().size() - lastSize);
		assertDoesNotThrow(() -> genreStorage.deleteItem(item[0].getId()));
		assertEquals(lastSize, genreStorage.getAllItems().size());
		assertThrows(KeyNotFoundException.class, () -> genreStorage.deleteItem(item[0].getId()));
	}

	@Test
	@Order(4)
	void testUserStorage() {
		final String LOGIN1 = Instant.now().toString();
		final String LOGIN2 = LOGIN1 + "+";
		List<User> userList = userStorage.getAllItems();
		int lastSize = userList.size();
		User item2Create = User.builder().name(LOGIN1).email(LOGIN1).login(LOGIN1).birthday(LocalDate.now().minusYears(1)).build();
		final User[] item = {userStorage.createItem(item2Create)};
		assertEquals(LOGIN1, item[0].getName());
		assertEquals(1, userStorage.getAllItems().size() - lastSize);
		assertThrows(DataAccessException.class, () -> userStorage.createItem(item2Create));
		item[0].setName(LOGIN2);
		assertDoesNotThrow(() -> item[0] = userStorage.updateItem(item[0]));
		assertEquals(1, userStorage.getAllItems().size() - lastSize);

		assertDoesNotThrow(() -> userStorage.deleteItem(item[0].getId()));
		assertEquals(lastSize, userStorage.getAllItems().size());
		assertThrows(KeyNotFoundException.class, () -> userStorage.deleteItem(item[0].getId()));
	}

	@Test
	@Order(5)
	void testUserStorageWithFriends() {
		final String NAME1 = Instant.now().toString();
		final String NAME2 = NAME1 + "+";
		final String NAME3 = NAME1 + "-";
		List<User> userList = userStorage.getAllItems();
		int lastSize = userList.size();
		User[] user1 = {User.builder().name(NAME1).email(NAME1).login(NAME1).birthday(LocalDate.now().minusYears(1)).build()};
		User[] user2 = {User.builder().name(NAME2).email(NAME2).login(NAME2).birthday(LocalDate.now().minusYears(1)).build()};
		User[] user3 = {User.builder().name(NAME3).email(NAME3).login(NAME3).birthday(LocalDate.now().minusYears(1)).build()};
		user1[0] = userStorage.createItem(user1[0]);
		user2[0] = userStorage.createItem(user2[0]);
		user3[0] = userStorage.createItem(user3[0]);
		assertNotEquals(user1[0].getId(), user2[0].getId());
		assertNotEquals(user1[0].getId(), user3[0].getId());
		assertEquals(0, userStorage.getFriends(user1[0].getId()).size());
		assertEquals(0, userStorage.getFriends(user2[0].getId()).size());
		assertEquals(0, userStorage.getFriends(user3[0].getId()).size());

		assertThrows(KeyNotFoundException.class, () -> userStorage.addFriend(user1[0].getId(), user1[0].getId()));

		assertDoesNotThrow(() -> userStorage.addFriend(user1[0].getId(), user3[0].getId()));
		assertEquals(1, userStorage.getFriends(user1[0].getId()).size());
		assertEquals(0, userStorage.getFriends(user2[0].getId()).size());
		assertEquals(0, userStorage.commonFriends(user1[0].getId(), user2[0].getId()).size());
		assertDoesNotThrow(() -> userStorage.addFriend(user2[0].getId(), user3[0].getId()));
		assertEquals(1, userStorage.commonFriends(user1[0].getId(), user2[0].getId()).size());

		assertEquals(0, userStorage.getFriends(user3[0].getId()).size());
		assertDoesNotThrow(() -> userStorage.addFriend(user3[0].getId(), user1[0].getId()));
		assertEquals(1, userStorage.getFriends(user3[0].getId()).size());
		assertDoesNotThrow(() -> userStorage.removeFriend(user3[0].getId(), user1[0].getId()));
		assertEquals(0, userStorage.getFriends(user3[0].getId()).size());

		assertThrows(KeyNotFoundException.class, () -> userStorage.addFriend(user1[0].getId(), UserIdType.of(-999L)));
		assertThrows(KeyNotFoundException.class, () -> userStorage.removeFriend(user1[0].getId(), UserIdType.of(-999L)));

	}

	@Test
	@Order(7)
	void testFilmStorage() {
		final String NAME1 = String.valueOf(LocalDate.now().getYear());
		final String NAME2 = NAME1 + "+";
		List<Film> filmList = filmStorage.getAllItems();
		int lastSize = filmList.size();
		Genre genre1 = genreStorage.createItem(Genre.builder().name(NAME1).build());
		RankMPA mpa1 = mpaStorage.createItem(RankMPA.builder().name(NAME1).build());
		Genre genre2 = genreStorage.createItem(Genre.builder().name(NAME2).build());
		RankMPA mpa2 = mpaStorage.createItem(RankMPA.builder().name(NAME2).build());
		FilmIdType[] dummyId = {null};
		Film item2Create = Film.builder()
				.name(NAME1)
				.description(NAME1)
				.releaseDate(LocalDate.now())
				.duration(100)
				.genres(List.of(genre1))
				.mpa(mpa1)
				.build();
		final Film[] item = {filmStorage.createItem(item2Create)};
		assertEquals(NAME1, item[0].getName());
		assertEquals(1, item[0].getGenres().size());
		assertEquals(NAME1, item[0].getGenres().get(0).getName());
		assertEquals(NAME1, item[0].getMpa().getName());
		assertEquals(1, filmStorage.getAllItems().size() - lastSize);

		item[0].setName(NAME2);
		item[0].setMpa(mpa2);
		item[0].getGenres().add(genre2);
		assertDoesNotThrow(() -> item[0] = filmStorage.updateItem(item[0]));
		assertEquals(1, filmStorage.getAllItems().size() - lastSize);
		assertEquals(NAME2, item[0].getName());
		assertEquals(2, item[0].getGenres().size());
		assertEquals(NAME1, item[0].getGenres().get(0).getName());
		assertEquals(NAME2, item[0].getGenres().get(1).getName());
		assertEquals(NAME2, item[0].getMpa().getName());

		dummyId[0] = FilmIdType.of(-999L);

		assertThrows(KeyNotFoundException.class, () -> filmStorage.deleteItem(dummyId[0]));

		dummyId[0] = item[0].getId();
		assertThrows(DataIntegrityViolationException.class, () -> filmStorage.deleteItem(dummyId[0]));

		item[0].getGenres().clear();
		assertDoesNotThrow(() -> item[0] = filmStorage.updateItem(item[0]));
		assertDoesNotThrow(() -> filmStorage.deleteItem(item[0].getId()));
		assertEquals(lastSize, filmStorage.getAllItems().size());

		assertThrows(KeyNotFoundException.class, () -> filmStorage.deleteItem(dummyId[0]));
	}

	@Test
	@Order(6)
	void testFilmStorageWithLikes() {
		final String NAME1 = String.valueOf(LocalDate.now().getYear());
		final String NAME2 = NAME1 + "+";

		int lastPopularCount = filmStorage.getPopular(100).size();

		RankMPA mpa = mpaStorage.getAllItems().get(0);
		Film[] film = {Film.builder().name(NAME1).description(NAME1).duration(100).releaseDate(LocalDate.now()).mpa(mpa).build()
				, Film.builder().name(NAME2).description(NAME2).duration(200).releaseDate(LocalDate.now()).mpa(mpa).build()};
		User user1 = User.builder().name(NAME1).login(NAME1).email(NAME1).birthday(LocalDate.now().minusYears(1)).build();
		User user2 = User.builder().name(NAME2).login(NAME2).email(NAME2).birthday(LocalDate.now().minusYears(1)).build();
		user1 = userStorage.createItem(user1);
		user2 = userStorage.createItem(user2);

		film[0] = filmStorage.createItem(film[0]);
		assertEquals(lastPopularCount + 1, filmStorage.getPopular(10).size());
		film[1] = filmStorage.createItem(film[1]);
		assertEquals(1, filmStorage.getPopular(1).size());
		assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
		filmStorage.addLike(film[1].getId(), user1.getId());
		assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
		assertEquals(film[1].getId(), filmStorage.getPopular(10).get(0).getId());
		assertEquals(film[0].getId(), filmStorage.getPopular(10).get(1).getId());

		assertThrows(DataIntegrityViolationException.class, () -> filmStorage.addLike(film[0].getId(), UserIdType.of(-999L)));

		filmStorage.addLike(film[0].getId(), user1.getId());
		filmStorage.addLike(film[0].getId(), user2.getId());
		assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
		assertEquals(film[0].getId(), filmStorage.getPopular(10).get(0).getId());
		assertEquals(film[1].getId(), filmStorage.getPopular(10).get(1).getId());

		filmStorage.removeLike(film[0].getId(), user1.getId());
		filmStorage.removeLike(film[0].getId(), user2.getId());
		assertEquals(lastPopularCount + 2, filmStorage.getPopular(10).size());
		assertEquals(film[1].getId(), filmStorage.getPopular(10).get(0).getId());
		assertEquals(film[0].getId(), filmStorage.getPopular(10).get(1).getId());
	}

}
