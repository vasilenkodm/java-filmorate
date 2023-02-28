package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

class FilmControllerTest {
    protected Film film;
    protected FilmController controller;

    protected static Validator validator;
    protected static ValidatorFactory validatorFactory;
    @BeforeAll
    public static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }
    /*
    @AfterAll
    public static void afterAll() {
        validatorFactory.close();
    }

    @BeforeEach
    void initFilm() {
        film = new Film(new FilmIdType(1L), "Film name", "Description", LocalDate.now(), 90*60);
        controller =  new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }
    @Test
    void testItemOperations() {
        Film blankItem = new Film();

        assertEquals(0, validator.validate(film).size(), "Объект неправильно инициализирован");

        assertNotEquals(0, validator.validate(blankItem).size());
        blankItem.setId(new FilmIdType( -1* film.getId().getValue()));
        assertNotEquals(0, validator.validate(blankItem).size());
        assertThrows(KeyNotFoundException.class, ()->controller.update(blankItem));

        assertEquals(0, controller.getAllFilms().size());

        assertDoesNotThrow(()->controller.create(film));
        assertEquals(1, controller.getAllFilms().size());


        assertDoesNotThrow(()->controller.update(film));
        assertEquals(1, controller.getAllFilms().size());

        assertNotEquals(0, validator.validate(blankItem).size());

    }

    @Test
    void shouldRejectName() {
        film.setName("");
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldRejectDescription() {
        StringBuilder sb = new StringBuilder();
        while (sb.length()<= MAX_DESCRIPTION_LENGTH) {
            sb.append("Another line of description.\n");
        }
        film.setDescription(sb.toString());
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldRejectReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895,12,28));
        assertEquals(0, validator.validate(film).size());

        film.setReleaseDate(film.getReleaseDate().minusDays(1));
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    void shouldRejectDuration() {
        film.setDuration(1);
        assertEquals(0, validator.validate(film).size());

        film.setDuration(0);
        assertEquals(1, validator.validate(film).size());
    }
    */
}