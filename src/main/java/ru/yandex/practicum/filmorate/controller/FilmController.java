package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private final IdGenerator idGenerator;

    public FilmController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        validateFilm(film);
        film.setId(idGenerator.nextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws IdNotFoundException, ValidationException {
        if (!films.containsKey(film.getId())) {
            log.error("Попытка обновления фильма с несуществующим id: {}", film.getId());
            throw new IdNotFoundException("Попытка обновления фильма с несуществующим id!");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм обновлен: {}", film);
        return film;
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film == null) {
            log.error("Тело запроса пустое (должен быть объект Film)");
            throw new ValidationException("Тело запроса пустое (должен быть объект Film)");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.error("Дата релиза не прошла валидацию: {}", film);
            throw new ValidationException("Дата релиза не прошла валидацию!");
        }
    }
}
