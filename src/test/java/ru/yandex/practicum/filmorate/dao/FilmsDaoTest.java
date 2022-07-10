package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class FilmsDaoTest {
    private final FilmsDao filmsDao;

    @Autowired
    public FilmsDaoTest(FilmsDao filmsDao) {
        this.filmsDao = filmsDao;
    }

    @Test
    public void checkGet() {
        Film film = filmsDao.get(2);

        assertEquals(2, film.getId());
        assertEquals("Бойцовский клуб", film.getName());
        assertEquals("У парня шиза", film.getDescription());
        assertEquals(LocalDate.of(2000, Month.JANUARY, 13), film.getReleaseDate());
        assertEquals(139, film.getDuration());
        assertEquals(5, film.getMpa().getId());
        assertEquals("NC-17", film.getMpa().getName());
        assertEquals(8, film.getRate());
        assertEquals(2, film.getGenres().size());
    }

    @Test
    public void checkGetAll() {
        List<Film> films = filmsDao.getAll();

        assertEquals(5, films.size());
    }

    @Test
    public void checkCreate() {
        Film film = getFilmWithoutId();
        long id = filmsDao.create(film).getId();

        Film createdFilm = filmsDao.get(id);

        assertEquals(film.getId(), createdFilm.getId());
        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(film.getDuration(), createdFilm.getDuration());
        assertEquals(film.getMpa().getId(), createdFilm.getMpa().getId());
        assertEquals(film.getMpa().getName(), createdFilm.getMpa().getName());
        assertEquals(film.getRate(), createdFilm.getRate());
        assertEquals(film.getGenres().size(), createdFilm.getGenres().size());
    }

    @Test
    public void checkUpdate() {
        Film film = filmsDao.get(4);

        updateFilm(film);

        filmsDao.update(film);
        Film updatedFilm = filmsDao.get(4);

        assertEquals(film.getId(), updatedFilm.getId());
        assertEquals(film.getName(), updatedFilm.getName());
        assertEquals(film.getDescription(), updatedFilm.getDescription());
        assertEquals(film.getReleaseDate(), updatedFilm.getReleaseDate());
        assertEquals(film.getDuration(), updatedFilm.getDuration());
        assertEquals(film.getMpa().getId(), updatedFilm.getMpa().getId());
        assertEquals(film.getMpa().getName(), updatedFilm.getMpa().getName());
        assertEquals(film.getRate(), updatedFilm.getRate());
        assertEquals(film.getGenres().size(), updatedFilm.getGenres().size());
    }

    @Test
    public void checkGetPopularSize6() {
        List<Film> popularFilms = filmsDao.getPopular(6);

        assertEquals(5, popularFilms.size());
        assertEquals("Титаник", popularFilms.get(0).getName());
        assertEquals("Шрек", popularFilms.get(1).getName());
        assertEquals("Бойцовский клуб", popularFilms.get(2).getName());
        assertEquals("Нет", popularFilms.get(3).getName());
        assertEquals("Евротур", popularFilms.get(4).getName());
    }

    @Test
    public void checkGetPopularSize3() {
        List<Film> popularFilms = filmsDao.getPopular(3);

        assertEquals(3, popularFilms.size());
        assertEquals("Титаник", popularFilms.get(0).getName());
        assertEquals("Шрек", popularFilms.get(1).getName());
        assertEquals("Бойцовский клуб", popularFilms.get(2).getName());
    }

    private Film getFilmWithoutId() {
        Set<Genre> genres = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        Mpa mpa = new Mpa(4, "R");
        Film testFilm = new Film();
        testFilm.setName("Джей и молчаливый Боб наносят ответный удар");
        testFilm.setDescription("Фильм о том, как Джей и молчаливый Боб наносят ответный удар Голливуду");
        testFilm.setReleaseDate(LocalDate.of(2001, Month.AUGUST, 24));
        testFilm.setDuration(104);
        testFilm.setGenres(genres);
        testFilm.setMpa(mpa);
        testFilm.setRate(6);
        return testFilm;
    }

    private void updateFilm(Film testFilm) {
        Set<Genre> genres = Set.of(new Genre(1, "Комедия"), new Genre(2, "Драма"));
        Mpa mpa = new Mpa(4, "R");
        testFilm.setName("Джей и молчаливый Боб наносят ответный удар");
        testFilm.setDescription("Фильм о том, как Джей и молчаливый Боб наносят ответный удар Голливуду");
        testFilm.setReleaseDate(LocalDate.of(2001, Month.AUGUST, 24));
        testFilm.setDuration(104);
        testFilm.setGenres(genres);
        testFilm.setMpa(mpa);
        testFilm.setRate(6);
    }
}
