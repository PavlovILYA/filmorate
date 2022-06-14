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
        log.info("/users/{} (GET)", id);
        return userService.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriends(@PathVariable("id") long id,
                            @PathVariable("friendId") long friendId) {
        log.info("/users/{}/friends/{} (PUT)", id, friendId);
        userService.makeFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void stopBeingFriends(@PathVariable("id") long id,
                                 @PathVariable("friendId") long friendId) {
        log.info("/users/{}/friends/{} (DELETE)", id, friendId);
        userService.stopBeingFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) {
        log.info("/users/{}/friends (GET)", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getFriends(@PathVariable("id") long id,
                                 @PathVariable("otherId") long otherId) {
        log.info("/users/{}/friends/common/{} (GET)", id, otherId);
        return userService.getCommonFriends(id, otherId);
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
