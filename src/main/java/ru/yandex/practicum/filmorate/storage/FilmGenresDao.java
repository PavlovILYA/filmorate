package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.util.List;

public interface FilmGenresDao {
    void create(FilmGenres filmGenres);
    void remove(FilmGenres filmGenres);
    List<FilmGenres> getByFilmId(long filmId);
}
