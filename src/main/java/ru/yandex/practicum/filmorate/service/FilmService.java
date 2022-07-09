package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.UserDao;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmDao filmStorage;
    private final UserDao userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDaoImpl") FilmDao filmStorage,
                       @Qualifier("userDaoImpl") UserDao userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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

    public void like(long userId, long filmId) {
//        User user = userStorage.get(userId);
//        Film film = filmStorage.get(filmId);
//        user.addLike(filmId);
//        film.addLike(userId); // update?
    }

    public void unlike(long userId, long filmId) {
//        User user = userStorage.get(userId);
//        Film film = filmStorage.get(filmId);
//        user.removeLike(filmId);
//        film.removeLike(userId); // update?
    }

    public List<Film> getPopularFilms(int count) {
//        return filmStorage.getAll().stream()
//                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
//                .limit(count)
//                .collect(Collectors.toList());
        return null;
    }
}
