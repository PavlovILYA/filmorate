package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserLike;

import java.util.List;

public interface UserLikeDao {
    void create(long filmId, long userId);
    void remove(long filmId, long userId);
}
