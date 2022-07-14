package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class GenresDaoTest {
    private final GenresDao genresDao;

    @Autowired
    public GenresDaoTest(GenresDao genresDao) {
        this.genresDao = genresDao;
    }

    @Test
    public void checkGet() {
        Genre genre = genresDao.get(3);

        assertEquals(3, genre.getId());
        assertEquals("Мультфильм", genre.getName());
    }

    @Test
    public void checkGetAll() {
        List<Genre> genres = genresDao.getAll();

        assertEquals(6, genres.size());
    }
}
