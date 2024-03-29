package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractIntegrationTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersDaoTest extends AbstractIntegrationTest {
    private final UsersDao usersDao;

    @Autowired
    public UsersDaoTest(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @Test
    public void checkGet() {
        User user = usersDao.get(1);

        assertEquals(1, user.getId());
        assertEquals("superduper@gmail.com", user.getEmail());
        assertEquals("superduper", user.getLogin());
        assertEquals("Игорь", user.getName());
        assertEquals(LocalDate.of(1998, Month.DECEMBER, 20), user.getBirthday());
    }

    @Test
    public void checkGetAll() {
        List<User> users = usersDao.getAll();

        assertEquals(5, users.size());
    }

    @Test
    public void checkCreate() {
        User user = getTestUserWithoutId();
        long id = usersDao.create(user).getId();
        User createdUser = usersDao.get(id);

        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getLogin(), createdUser.getLogin());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
    }

    @Test
    public void checkUpdate() {
        User user = usersDao.get(2);

        updateUser(user);

        usersDao.update(user);
        User updatedUser = usersDao.get(2);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getLogin(), updatedUser.getLogin());
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getBirthday(), updatedUser.getBirthday());
    }

    private User getTestUserWithoutId() {
        User testUser = new User();
        testUser.setName("Имя 1");
        testUser.setBirthday(LocalDate.of(1999, Month.JUNE, 17));
        testUser.setEmail("i.e.pavlov@ya.ru");
        testUser.setLogin("oh_pavlov");
        return testUser;
    }

    private void updateUser(User testUser) {
        testUser.setName("Николай");
        testUser.setLogin("nick");
        testUser.setEmail("nicknick@pohcta.ru");
        testUser.setBirthday(LocalDate.of(1989, Month.SEPTEMBER, 12));
    }
}
