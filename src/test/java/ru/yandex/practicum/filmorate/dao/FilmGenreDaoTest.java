package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractIntegrationTest;
import ru.yandex.practicum.filmorate.model.FilmGenres;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmGenreDaoTest extends AbstractIntegrationTest {
    private final FilmGenreDao filmGenreDao;
    private final FilmsDao filmsDao;

    @Autowired
    public FilmGenreDaoTest(FilmGenreDao filmGenreDao, FilmsDao filmsDao) {
        this.filmGenreDao = filmGenreDao;
        this.filmsDao = filmsDao;
    }

    @Test
    public void checkCreate() {
        // добавляем Шреку жанр "Комедия"
        FilmGenres filmGenres = new FilmGenres(4, 1);
        filmGenreDao.create(filmGenres);

        List<FilmGenres> filmGenresOfShrek = filmGenreDao.getByFilmId(4);

        // теперь 2 жанра
        assertEquals(2, filmGenresOfShrek.size());
    }

    @Test
    public void checkRemove() {
        // убираем Шреку жанр "Мультфильм"
        FilmGenres filmGenres = new FilmGenres(4, 3);
        filmGenreDao.remove(filmGenres);

        List<FilmGenres> filmGenresOfShrek = filmGenreDao.getByFilmId(4);

        // теперь 0 жанров
        assertEquals(0, filmGenresOfShrek.size());
    }

    @Test
    public void checkGetByFilmId() {
        List<FilmGenres> filmGenresOfShrek = filmGenreDao.getByFilmId(4);

        assertEquals(1, filmGenresOfShrek.size());
        assertEquals(new FilmGenres(4, 3), filmGenresOfShrek.get(0));
    }
}
