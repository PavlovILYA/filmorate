package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class FilmGenresDaoTest {
    private final FilmGenresDao filmGenresDao;
    private final FilmDao filmDao;

    @Autowired
    public FilmGenresDaoTest(FilmGenresDao filmGenresDao, FilmDao filmDao) {
        this.filmGenresDao = filmGenresDao;
        this.filmDao = filmDao;
    }

    @Test
    public void checkCreate() {
        // добавляем Шреку жанр "Комедия"
        FilmGenres filmGenres = new FilmGenres(4, 1);
        filmGenresDao.create(filmGenres);

        List<FilmGenres> filmGenresOfShrek = filmGenresDao.getByFilmId(4);

        // теперь 2 жанра
        assertEquals(2, filmGenresOfShrek.size());
    }

    @Test
    public void checkRemove() {
        // убираем Шреку жанр "Мультфильм"
        FilmGenres filmGenres = new FilmGenres(4, 3);
        filmGenresDao.remove(filmGenres);

        List<FilmGenres> filmGenresOfShrek = filmGenresDao.getByFilmId(4);

        // теперь 0 жанров
        assertEquals(0, filmGenresOfShrek.size());
    }

    @Test
    public void checkGetByFilmId() {
        List<FilmGenres> filmGenresOfShrek = filmGenresDao.getByFilmId(4);

        assertEquals(1, filmGenresOfShrek.size());
        assertEquals(new FilmGenres(4, 3), filmGenresOfShrek.get(0));
    }
}
