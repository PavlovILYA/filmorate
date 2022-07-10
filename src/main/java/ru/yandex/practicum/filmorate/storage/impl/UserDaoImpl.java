package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO filmorate_user (email, login, name, birthday) \n" +
                "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
                PreparedStatement ps =
                        connection.prepareStatement(sqlQuery, new String[] {"id"});
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setDate(4, Date.valueOf(user.getBirthday()));
                return ps;
        }, keyHolder);
        log.info("genereted key: {}", keyHolder.getKey());
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE filmorate_user \n" +
                "SET email = ?, login = ?, name = ?, birthday = ? \n" +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        return get(user.getId());
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM filmorate_user";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildUser(resultSet));
    }

    @Override
    public User get(long userId) {
        String sqlQuery = "SELECT id, email, login, name, birthday FROM filmorate_user \n" +
                "WHERE id = ?;";
        List<User> users = jdbcTemplate.query(sqlQuery,
                (resultSet, rowId) -> buildUser(resultSet), userId);
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        }
        return users.get(0);
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate());
    }
}