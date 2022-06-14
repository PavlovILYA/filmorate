package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("/users (GET)");
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("/users (POST): {}", user);
        validateUser(user);
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("/users (PUT): {}", user);
        validateUser(user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") long id) {
        log.info("/users{} (GET)", id);
        return userService.get(id);
    }

    private void validateUser(User user) {
        if (user == null) {
            log.error("Тело запроса пустое (должен быть объект User)");
            throw new ValidationException("Тело запроса пустое (должен быть объект User)");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Логин содержит пробелы: {}", user);
            throw new ValidationException("Логин содержит пробелы!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Имя пустое -> проставляем логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
