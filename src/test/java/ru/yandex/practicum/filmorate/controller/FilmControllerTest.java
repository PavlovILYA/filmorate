package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {
    private final IdGenerator idGenerator = new IdGenerator();
    private final FilmController filmController = new FilmController(idGenerator);
    private Film testFilm;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        testFilm = new Film(); // приемлемые значения
        testFilm.setName("Имя 1");
        testFilm.setDescription("Описание 1");
        testFilm.setReleaseDate(LocalDate.now());
        testFilm.setDuration(100);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void checkNullFilmName() {
        testFilm.setName(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(2, violations.size());
        for (ConstraintViolation<Film> violation : violations) {
            if (violation.getMessageTemplate().equals("{javax.validation.constraints.NotNull.message}")) {
                assertEquals("не должно равняться null", violation.getMessage());
                assertEquals("name", violation.getPropertyPath().toString());
            }
            if (violation.getMessageTemplate().equals("{javax.validation.constraints.NotBlank.message}")) {
                assertEquals("не должно быть пустым", violation.getMessage());
                assertEquals("name", violation.getPropertyPath().toString());
            }
        }
    }

    @Test
    public void checkEmptyFilmName() {
        testFilm.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<Film> violation : violations) {
            assertEquals("не должно быть пустым", violation.getMessage());
            assertEquals("name", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void check200SymbolsFilmDescription() {
        testFilm.setDescription("200 symbols:        " +
                "************************************************************" +
                "************************************************************" +
                "************************************************************");

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(0, violations.size());
    }

    @Test
    public void checkMoreThan200SymbolsFilmDescription() {
        testFilm.setDescription("more than 200 symbols; more than 200 symbols; more than 200 symbols; " +
                "more than 200 symbols; more than 200 symbols; more than 200 symbols; " +
                "more than 200 symbols; more than 200 symbols; more than 200 symbols; " +
                "more than 200 symbols; more than 200 symbols; more than 200 symbols; ");

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<Film> violation : violations) {
            assertEquals("размер должен находиться в диапазоне от 0 до 200", violation.getMessage());
            assertEquals("description", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkNegativeFilmDuration() {
        testFilm.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<Film> violation : violations) {
            assertEquals("должно быть больше 0", violation.getMessage());
            assertEquals("duration", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkZeroFilmDuration() {
        testFilm.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(1, violations.size());
        for (ConstraintViolation<Film> violation : violations) {
            assertEquals("должно быть больше 0", violation.getMessage());
            assertEquals("duration", violation.getPropertyPath().toString());
        }
    }

    @Test
    public void checkPositiveFilmDuration() {
        testFilm.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        assertEquals(0, violations.size());
    }

    @Test
    public void checkEarlierFilmReleaseDate() {
        testFilm.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.createFilm(testFilm);
        });
        assertEquals("Дата релиза не прошла валидацию!", exception.getMessage());
    }

    @Test
    public void checkNullFilm() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.createFilm(null);
        });
        assertEquals("Тело запроса пустое (должен быть объект Film)", exception.getMessage());
    }
}
