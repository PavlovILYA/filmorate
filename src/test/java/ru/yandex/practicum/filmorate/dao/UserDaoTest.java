package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class UserDaoTest {
    private final UserDao userDao;

    @Autowired
    public UserDaoTest(UserDao userDao) {
        this.userDao = userDao;
    }

    @Test
    public void checkGet() {
        User user = userDao.get(1);

        assertEquals(1, user.getId());
        assertEquals("superduper@gmail.com", user.getEmail());
        assertEquals("superduper", user.getLogin());
        assertEquals("Игорь", user.getName());
        assertEquals(LocalDate.of(1998, Month.DECEMBER, 20), user.getBirthday());
    }

    @Test
    public void checkGetAll() {
        List<User> users = userDao.getAll();

        assertEquals(5, users.size());
    }

    @Test
    public void checkCreate() {
        User user = getTestUserWithoutId("Имя 1", "i.e.pavlov@ya.ru",
                "oh_pavlov", LocalDate.of(1999, Month.JUNE, 17));
        long id = userDao.create(user).getId();
        User returnedUser = userDao.get(id);

        assertEquals(user.getEmail(), returnedUser.getEmail());
        assertEquals(user.getLogin(), returnedUser.getLogin());
        assertEquals(user.getName(), returnedUser.getName());
        assertEquals(user.getBirthday(), returnedUser.getBirthday());
    }

    @Test
    public void checkUpdate() {
        User user = userDao.get(2);
        user.setName("Николай");
        user.setLogin("nick");
        user.setEmail("nicknick@pohcta.ru");
        user.setBirthday(LocalDate.of(1989, Month.SEPTEMBER, 12));
        userDao.update(user);
        User returnedUser = userDao.get(2);

        assertEquals(user.getId(), returnedUser.getId());
        assertEquals(user.getEmail(), returnedUser.getEmail());
        assertEquals(user.getLogin(), returnedUser.getLogin());
        assertEquals(user.getName(), returnedUser.getName());
        assertEquals(user.getBirthday(), returnedUser.getBirthday());
    }

    private User getTestUserWithoutId(String name, String email, String login, LocalDate birthday) {
        User testUser = new User();
        testUser.setName(name);
        testUser.setBirthday(birthday);
        testUser.setEmail(email);
        testUser.setLogin(login);
        return testUser;
    }
}
