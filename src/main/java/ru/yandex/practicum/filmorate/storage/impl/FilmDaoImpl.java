package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, MpaDao mpaDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO film " +
                "(name, description, release_date, duration, mpa_id, rate)\n" +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((connection) -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[] {"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            ps.setInt(6, film.getRate());
            return ps;
        }, keyHolder);
        log.info("genereted key: {}", keyHolder.getKey());
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM film;";
        return jdbcTemplate.query(sqlQuery, (resultSet, rowId) -> buildFilm(resultSet));
    }

    @Override
    public Film get(long filmId) {
        String sqlQuery = "SELECT * FROM film WHERE id = ?;";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery,
                    (resultSet, rowId) -> buildFilm(resultSet), filmId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new FilmNotFoundException();
        }
        return film;
    }

    public Film buildFilm(ResultSet resultSet) throws SQLException {
        return new Film(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                null,
                mpaDao.get(resultSet.getLong("mpa_id")),
                resultSet.getInt("rate"));
    }
}
