package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.dao.UserLikeDao;

@Component
public class UserLikeDaoImpl implements UserLikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(long filmId, long userId) {
        String sqlQuery = "INSERT INTO user_like (user_id, film_id) " +
                "VALUES (?, ?);";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void remove(long filmId, long userId) {
        String sqlQuery = "DELETE FROM user_like " +
                "WHERE user_id = ? AND film_id = ?;";
        int amountOfUpdated = jdbcTemplate.update(sqlQuery, userId, filmId);
        if (amountOfUpdated != 1) {
            throw new UserNotFoundException();
        }
    }
}
