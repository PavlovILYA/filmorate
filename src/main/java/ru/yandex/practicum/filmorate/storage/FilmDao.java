package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    Film create(Film film);
    Film update(Film film);
    void delete(Film film);
    List<Film> getAll();
    Film get(long filmId);
    List<Film> getPopular(int size);
}