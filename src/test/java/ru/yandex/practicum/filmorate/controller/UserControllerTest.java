package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

class UserControllerTest {
    protected User user;
    protected UserController controller;

    protected static Validator validator;
    protected static ValidatorFactory validatorFactory;
    @BeforeAll
    public static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }
    @AfterAll
    public static void afterAll() {
        validatorFactory.close();
    }
    /*
    @BeforeEach
    void initFilm() {
        user = new User(new UserIdType(1L), "user@domaim.com", "user", "name", LocalDate.now().minusYears(20));
        controller =  new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void testItemOperations() {
        User blankItem = new User();

        assertEquals(0, validator.validate(user).size(), "Объект неправильно инициализирован");

        assertNotEquals(0, validator.validate(blankItem).size());
        blankItem.setId(new UserIdType(-1* user.getId().getValue()));
        assertNotEquals(0, validator.validate(blankItem).size());
        assertThrows(KeyNotFoundException.class, ()->controller.update(blankItem));

        assertEquals(0, controller.getAllUsers().size());

        assertDoesNotThrow(()->controller.create(user));
        assertEquals(1, controller.getAllUsers().size());


        assertDoesNotThrow(()->controller.update(user));
        assertEquals(1, controller.getAllUsers().size());

        assertNotEquals(0, validator.validate(blankItem).size());

    }


    @Test
    void shouldRejectEmail() {
        user.setEmail(null);
        assertEquals(1, validator.validate(user).size());

        user.setEmail("user@");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    void shouldRejectLogin() {
        user.setLogin("");
        assertEquals(1, validator.validate(user).size());

        user.setLogin(" ");
        assertNotEquals(0, validator.validate(user).size());

        user.setLogin("The user");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    void shouldRejectBirthday() {
        user.setBirthday(LocalDate.now().minusDays(1));
        assertEquals(0, validator.validate(user).size());

        user.setBirthday(LocalDate.now().plusDays(1));
        assertEquals(1, validator.validate(user).size());
    }

     */
}