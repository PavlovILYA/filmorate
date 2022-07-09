package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDaoImp implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(long id) {
        String sqlQuery = "SELECT * FROM genre WHERE id = ?;";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, (resultSet, rowId) -> buildGenre(resultSet), id);
        } catch(IncorrectResultSizeDataAccessException e) {
            throw new GenreNotFoundException();
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genre;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildGenre(resultSet));
    }

    private Genre buildGenre(ResultSet resultSet) throws SQLException {
        return new Genre(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
