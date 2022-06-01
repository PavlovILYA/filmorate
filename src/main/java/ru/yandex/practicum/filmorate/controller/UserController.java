package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    public UserController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        user.setId(idGenerator.nextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws IdNotFoundException, ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Попытка обновления пользователя с несуществующим id: {}", user.getId());
            throw new IdNotFoundException("Попытка обновления пользователя с несуществующим id!");
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен: {}", user);
        return user;
    }

    private void validateUser(User user) throws ValidationException {
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
