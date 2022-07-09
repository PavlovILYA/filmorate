package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);
    User update(User user);
    void delete(User user);
    List<User> getAll();
    User get(long userId);
    List<User> getFriends(long userId);
    List<User> getCommonFriends(long userId1, long userId2);
    void makeFriends(long userId1, long userId2);
    void stopBeingFriends(long userId1, long userId2);
}
