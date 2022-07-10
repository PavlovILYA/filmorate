package ru.yandex.practicum.filmorate.dao;


public interface UserFilmsDao {
    void create(long filmId, long userId);
    void remove(long filmId, long userId);
}
