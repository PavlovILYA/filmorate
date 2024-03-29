package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Tag(name = "Film-контроллер", description = "Позволяет управлять фильмами")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Получить все фильмы")
    public List<Film> getFilms() {
        log.info("/films (GET)");
        return filmService.getAll();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Создать фильм")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("/films (POST): {}", film);
        validateFilm(film);
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Изменить фильм")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("/films (PUT): {}", film);
        validateFilm(film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Получить фильм по id")
    public Film getFilm(@PathVariable("id") long id) {
        log.info("/films/{} (GET)", id);
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Поставить лайк от пользователя")
    public void like(@PathVariable("id") long id,
                     @PathVariable("userId") long userId) {
        log.info("/films/{}/like/{} (PUT)", id, userId);
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "Убрать лайк от пользователя")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable("id") long id,
                     @PathVariable("userId") long userId) {
        log.info("/films/{}/like/{} (DELETE)", id, userId);
        filmService.unlike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Получить самые популярные фильмы")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.info("/films/popular?count={} (GET)", count);
        return filmService.getPopularFilms(count);
    }

    private void validateFilm(Film film) {
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
