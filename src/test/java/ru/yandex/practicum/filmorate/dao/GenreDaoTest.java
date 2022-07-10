package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class GenreDaoTest {
    private final GenreDao genreDao;

    @Autowired
    public GenreDaoTest(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Test
    public void checkGet() {
        Genre genre = genreDao.get(3);

        assertEquals(3, genre.getId());
        assertEquals("Мультфильм", genre.getName());
    }

    @Test
    public void checkGetAll() {
        List<Genre> genres = genreDao.getAll();

        assertEquals(6, genres.size());
    }
}
