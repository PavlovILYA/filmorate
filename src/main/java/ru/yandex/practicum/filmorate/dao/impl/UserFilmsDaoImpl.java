package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.dao.UserFilmsDao;

@Component
public class UserFilmsDaoImpl implements UserFilmsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserFilmsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(long filmId, long userId) {
        String sqlQuery = "INSERT INTO user_films (user_id, film_id) " +
                "VALUES (?, ?);";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void remove(long filmId, long userId) {
        String sqlQuery = "DELETE FROM user_films " +
                "WHERE user_id = ? AND film_id = ?;";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }
}
