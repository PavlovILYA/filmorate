package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendshipRequestNotFound;
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

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "-- вывести список друзей (которые сами подали заявку)\n" +
                "-- т.к. дружба односторонняя, неважно подтверждена ли заявка\n" +
                "SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
                "FROM filmorate_user AS u1\n" +
                "         JOIN friendship AS f1 ON u1.id = f1.passive_user_id\n" +
                "         JOIN filmorate_user AS u2 ON u2.id = f1.active_user_id\n" +
                "WHERE u1.id = ?\n" +
                "\n" +
                "UNION\n" +
                "\n" +
                "-- вывести список пользователей, которые подтвердили заявку на дружбу\n" +
                "SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
                "FROM filmorate_user AS u1\n" +
                "         JOIN friendship AS f1 ON u1.id = f1.active_user_id\n" +
                "         JOIN filmorate_user AS u2 ON u2.id = f1.passive_user_id\n" +
                "WHERE u1.id = ? AND is_accepted = true;";
        return jdbcTemplate.query(sqlQuery,
                (resultSet, rowId) -> buildUser(resultSet), userId, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId1, long userId2) {
        Set<User> friendsOfUser1 = new HashSet<>(getFriends(userId1));
        Set<User> friendsOfUser2 = new HashSet<>(getFriends(userId2));
        friendsOfUser1.retainAll(friendsOfUser2);
        return new ArrayList<>(friendsOfUser1);

//        String sqlQuery = "(\n" +
//                "    -- друзья пользователя [id1], которым он сам отправил заявку\n" +
//                "    SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
//                "    FROM filmorate_user AS u1\n" +
//                "             JOIN friendship AS f1 ON u1.id = f1.active_user_id\n" +
//                "             JOIN filmorate_user AS u2 ON u2.id = f1.passive_user_id\n" +
//                "    WHERE u1.id = ? AND is_accepted = true\n" +
//                "\n" +
//                "    UNION\n" +
//                "\n" +
//                "    -- друзья пользователя [id1], заявку от которых он подтвердил\n" +
//                "    SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
//                "    FROM filmorate_user AS u1\n" +
//                "        JOIN friendship AS f1 ON u1.id = f1.passive_user_id\n" +
//                "        JOIN filmorate_user AS u2 ON u2.id = f1.active_user_id\n" +
//                "    WHERE u1.id = ? AND is_accepted = true\n" +
//                ")\n" +
//                "\n" +
//                "INTERSECT\n" +
//                "\n" +
//                "(\n" +
//                "    -- друзья пользователя [id2], которым он сам отправил заявку\n" +
//                "    SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
//                "    FROM filmorate_user AS u1\n" +
//                "             JOIN friendship AS f1 ON u1.id = f1.active_user_id\n" +
//                "             JOIN filmorate_user AS u2 ON u2.id = f1.passive_user_id\n" +
//                "    WHERE u1.id = ? AND is_accepted = true\n" +
//                "\n" +
//                "    UNION\n" +
//                "\n" +
//                "    -- друзья пользователя [id2], заявку от которых он подтвердил\n" +
//                "    SELECT u2.id, u2.email, u2.login, u2.name, u2.birthday\n" +
//                "    FROM filmorate_user AS u1\n" +
//                "        JOIN friendship AS f1 ON u1.id = f1.passive_user_id\n" +
//                "        JOIN filmorate_user AS u2 ON u2.id = f1.active_user_id\n" +
//                "    WHERE u1.id = ? AND is_accepted = true\n" +
//                ");";
//        return jdbcTemplate.query(sqlQuery,
//                (resultSet, rowId) -> buildUser(resultSet), userId1, userId1, userId2, userId2);
    }

    @Override
    public void makeFriends(long userId1, long userId2) {
        try {
            boolean status = getFriendshipRequestStatus(userId2, userId1);
            if (!status) {
                // заявка от второго пользователя еще не одобрена – одобряем
                acceptFriendshipRequest(userId2, userId1);
            }
        } catch (FriendshipRequestNotFound e) {
            // заявки от второго пользователя нет
            try {
                boolean status = getFriendshipRequestStatus(userId1, userId2);
                // заявка от первого пользователя уже есть – ничего не делаем
            } catch (FriendshipRequestNotFound ex) {
                // заявки от первого пользователя нет – создаем
                makeFriendshipRequest(userId1, userId2);
            }
        }
    }

    @Override
    public void stopBeingFriends(long userId1, long userId2) {

    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate());
    }

    private Boolean getFriendshipRequestStatus(long activeUserId, long passiveUserId) throws FriendshipRequestNotFound {
        String sqlQuery = "SELECT is_accepted FROM friendship\n" +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, activeUserId, passiveUserId);
        if (!rowSet.next()) {
            throw new FriendshipRequestNotFound();
        }
        return rowSet.getBoolean("is_accepted");
    }

    private void acceptFriendshipRequest(long activeUserId, long passiveUserId) {
        String sqlQuery = "UPDATE friendship SET is_accepted = TRUE\n" +
                "WHERE active_user_id = ? AND passive_user_id = ?;";
        jdbcTemplate.update(sqlQuery, activeUserId, passiveUserId);
    }

    private void makeFriendshipRequest(long activeUserId, long passiveUserId) {
        String sqlQuery = "INSERT INTO friendship (active_user_id, passive_user_id, is_accepted) \n" +
                "VALUES (?, ?, FALSE);";
        jdbcTemplate.update(sqlQuery, activeUserId, passiveUserId);
    }

}
