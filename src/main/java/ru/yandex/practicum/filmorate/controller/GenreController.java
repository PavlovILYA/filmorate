package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@Tag(name = "Genre-контроллер", description = "Позволяет управлять жанрами")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить жанр")
    public Genre getGenre(@PathVariable("id") long id) {
        log.info("Запрошен жанр: {}", id);
        return genreService.getGenre(id);
    }

    @GetMapping
    @Operation(summary = "Получить все жанры")
    public List<Genre> getAllGenres() {
        return genreService.getGenres();
    }
}
