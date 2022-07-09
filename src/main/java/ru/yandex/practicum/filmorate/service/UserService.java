package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDao;

import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserDao userStorage;

    @Autowired
    public UserService(@Qualifier("userDaoImpl") UserDao userStorage) {
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

    public User get(long id) {
        log.info("Получение пользователя {}", id);
        return userStorage.get(id);
    }

    public void makeFriends(long userId1, long userId2) {
        userStorage.makeFriends(userId1, userId2);
//        User user1 = userStorage.get(userId1);
//        User user2 = userStorage.get(userId2);
//        user1.addFriend(userId2);
//        user2.addFriend(userId1); // update?
    }

    public void stopBeingFriends(long userId1, long userId2) {
//        User user1 = userStorage.get(userId1);
//        User user2 = userStorage.get(userId2);
//        user1.removeFriend(userId2);
//        user2.removeFriend(userId1); // update?
    }

    public List<User> getFriends(long userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        return userStorage.getCommonFriends(userId1, userId2);
    }
}
