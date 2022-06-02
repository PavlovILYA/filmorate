package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    private final IdGenerator idGenerator = new IdGenerator();
    private final UserController userController = new UserController(idGenerator);
    private User testUser;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        testUser = new User(); // приемлемые значения
        testUser.setName("Имя 1");
        testUser.setBirthday(LocalDate.of(1999, Month.JUNE, 17));
        testUser.setEmail("i.e.pavlov@ya.ru");
        testUser.setLogin("oh_pavlov");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void checkNullEmail() {
        testUser.setEmail(null);

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("не должно равняться null", violation.getMessage());
            assertEquals("email", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkWrongFormatEmail() {
        testUser.setEmail("wrong_email"); // http://www.ex-parrot.com/~pdw/Mail-RFC822-Address.html

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("должно иметь формат адреса электронной почты", violation.getMessage());
            assertEquals("email", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkNullLogin() {
        testUser.setLogin(null);

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(2, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            if (violation.getMessageTemplate().equals("{javax.validation.constraints.NotNull.message}")) {
                assertEquals("не должно равняться null", violation.getMessage());
                assertEquals("login", violation.getPropertyPath().toString());
            }
            if (violation.getMessageTemplate().equals("{javax.validation.constraints.NotBlank.message}")) {
                assertEquals("не должно быть пустым", violation.getMessage());
                assertEquals("login", violation.getPropertyPath().toString());
            }
        }
    }

    @Test
    public void checkEmptyLogin() {
        testUser.setLogin("");

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("не должно быть пустым", violation.getMessage());
            assertEquals("login", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkLoginWithSpace() {
        testUser.setLogin("oh pavlov");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.createUser(testUser);
        });
        assertEquals("Логин содержит пробелы!", exception.getMessage());
    }

    @Test
    public void checkNullName() throws ValidationException {
        testUser.setName(null);

        User responseUser = userController.createUser(testUser);
        assertEquals(testUser.getLogin(), responseUser.getName());
    }

    @Test
    public void checkEmptyName() throws ValidationException {
        testUser.setName("");

        User responseUser = userController.createUser(testUser);
        assertEquals(testUser.getLogin(), responseUser.getName());
    }

    @Test
    public void checkPresentBirthday() {
        testUser.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("должно содержать прошедшую дату", violation.getMessage());
            assertEquals("birthday", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkFutureBirthday() {
        testUser.setBirthday(LocalDate.now().plusMonths(1));

        Set<ConstraintViolation<User>> violations = validator.validate(testUser);
        assertEquals(1, violations.size());
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("должно содержать прошедшую дату", violation.getMessage());
            assertEquals("birthday", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkNullUser() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.createUser(null);
        });
        assertEquals("Тело запроса пустое (должен быть объект User)", exception.getMessage());
    }
}
