package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@Tag(name = "Mpa-контроллер", description = "Позволяет управлять рейтингами MPA")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить тип MPA")
    public Mpa getMpa(@PathVariable("id") long id) {
        log.info("Запрошен mpa: {}", id);
        return mpaService.getMpa(id);
    }

    @GetMapping
    @Operation(summary = "Получить все типы MPA")
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }
}
