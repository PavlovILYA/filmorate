package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
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

    public void addLike(long userId, long filmId) {
        User user = userStorage.get(userId);
        Film film = filmStorage.get(filmId);
        user.addLike(filmId);
        film.addLike(userId); // update?
    }

    public void removeLike(long userId, long filmId) {
        User user = userStorage.get(userId);
        Film film = filmStorage.get(filmId);
        user.removeLike(filmId);
        film.removeLike(userId); // update?
    }

    public List<Film> getPopularFilms() {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
