package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendshipRequestNotFound;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.UserDao;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserService(@Qualifier("userDaoImpl") UserDao userDao, FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
    }

    public User create(User user) {
        log.info("Создание пользователя: {}", user);
        return userDao.create(user);
    }

    public User update(User user) {
        log.info("Обновление пользователя: {}", user);
        return userDao.update(user);
    }

    public List<User> getAll() {
        log.info("Получение всех пользователей");
        return userDao.getAll();
    }

    public User get(long id) {
        log.info("Получение пользователя {}", id);
        return userDao.get(id);
    }

    public void makeFriends(long userId1, long userId2) {
        try {
            Friendship friendship = friendshipDao.get(userId2, userId1);
            if (!friendship.isAccepted()) {
                friendshipDao.update(userId2, userId1, true);
            }
        } catch (FriendshipRequestNotFound e1) {
            try {
                friendshipDao.get(userId1, userId2);
            } catch (FriendshipRequestNotFound e2) {
                friendshipDao.create(userId1, userId2);
            }
        }
    }

    public void stopBeingFriends(long userId1, long userId2) {
        try {
            friendshipDao.get(userId2, userId1);
            friendshipDao.update(userId2, userId1, false);
        } catch (FriendshipRequestNotFound e1) {
            try {
                friendshipDao.get(userId1, userId2);
                friendshipDao.remove(userId1, userId2);
                friendshipDao.create(userId2, userId1);
            } catch (FriendshipRequestNotFound ignored) {
            }
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundException();
        }
    }

    public List<User> getFriends(long userId) {
        return friendshipDao.getByUserId(userId).stream()
                        .map(friendship -> getFriedFromFriendship(friendship, userId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        Set<User> friendsOfUser1 = new HashSet<>(getFriends(userId1));
        Set<User> friendsOfUser2 = new HashSet<>(getFriends(userId2));
        friendsOfUser1.retainAll(friendsOfUser2);
        return new ArrayList<>(friendsOfUser1);
    }

    private User getFriedFromFriendship(Friendship friendship, long userId) {
        if (friendship.getActiveUserId() == userId) {
            return userDao.get(friendship.getPassiveUserId());
        } else if (friendship.isAccepted()) {
            return userDao.get(friendship.getActiveUserId());
        } else {
            return null;
        }
    }
}
