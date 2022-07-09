package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    Genre get(long id);
    List<Genre> getAll();
}
