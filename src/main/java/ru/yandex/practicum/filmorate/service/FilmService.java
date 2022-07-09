package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.UserLikeDao;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmDao filmStorage;
    private final UserLikeDao userLikeDao;

    @Autowired
    public FilmService(@Qualifier("filmDaoImpl") FilmDao filmStorage, UserLikeDao userLikeDao) {
        this.filmStorage = filmStorage;
        this.userLikeDao = userLikeDao;
    }

    public Film create(Film film) {
        log.info("Создание фильма: {}", film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        log.info("Обновление фильма: {}", film);
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        log.info("Получение всех фильмов");
        return filmStorage.getAll();
    }

    public Film get(long id) {
        log.info("Получение фильма {}", id);
        return filmStorage.get(id);
    }

    public void like(long filmId, long userId) {
        userLikeDao.create(filmId, userId);
    }

    public void unlike(long filmId, long userId) {
        userLikeDao.remove(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage.getPopular(count);
        if (popularFilms.isEmpty()) {
            return filmStorage.getAll().stream()
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return popularFilms;
        }
    }
}
