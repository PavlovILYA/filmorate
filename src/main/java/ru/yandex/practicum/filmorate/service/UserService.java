package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        log.info("Создание пользователя: {}", user);
        return userStorage.create(user);
    }

    public User update(User user) {
        log.info("Обновление пользователя: {}", user);
        return userStorage.update(user);
    }

    public List<User> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.getAll();
    }

    public void makeFriends(long userId1, long userId2) {
        User user1 = userStorage.get(userId1);
        User user2 = userStorage.get(userId2);
        user1.addFriend(userId2);
        user2.addFriend(userId1);
    }

    public void stopBeingFriends(long userId1, long userId2) {
        User user1 = userStorage.get(userId1);
        User user2 = userStorage.get(userId2);
        user1.removeFriend(userId2);
        user2.removeFriend(userId1);
    }

    public List<User> getFriends(long userId) {
        List<User> friends = new ArrayList<>();
        return userStorage.get(userId).getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
