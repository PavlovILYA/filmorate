package ru.yandex.practicum.filmorate.dao;


public interface UserFilmDao {
    void create(long filmId, long userId);
    void remove(long filmId, long userId);
}
