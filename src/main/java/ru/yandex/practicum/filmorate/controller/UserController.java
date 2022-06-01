package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    public UserController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(idGenerator.nextId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
