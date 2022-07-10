package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FriendshipRequestNotFound;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class FriendshipDaoTest {
    private final FriendshipDao friendshipDao;

    @Autowired
    public FriendshipDaoTest(FriendshipDao friendshipDao) {
        this.friendshipDao = friendshipDao;
    }

    @Test
    public void checkGet() throws FriendshipRequestNotFound {
        Friendship friendship = friendshipDao.get(4, 5);

        assertEquals(4, friendship.getActiveUserId());
        assertEquals(5, friendship.getPassiveUserId());
        assertTrue(friendship.isAccepted());
    }

    @Test
    public void checkGetByUserId() {
        List<Friendship> friendships = friendshipDao.getByUserId(1);

        assertEquals(3, friendships.size());
    }

    @Test
    public void checkCreate() throws FriendshipRequestNotFound {
        friendshipDao.create(5, 1);

        Friendship friendship = friendshipDao.get(5, 1);

        assertEquals(5, friendship.getActiveUserId());
        assertEquals(1, friendship.getPassiveUserId());
        assertFalse(friendship.isAccepted());
    }

    @Test
    public void checkUpdate() throws FriendshipRequestNotFound {
        friendshipDao.update(1, 2, false);

        Friendship friendship = friendshipDao.get(1, 2);

        assertEquals(1, friendship.getActiveUserId());
        assertEquals(2, friendship.getPassiveUserId());
        assertFalse(friendship.isAccepted());
    }

    @Test
    public void checkRemove() {
        friendshipDao.remove(1, 2);

        assertThrows(FriendshipRequestNotFound.class,
                () -> friendshipDao.get(1, 2));
    }

    @Test
    public void checkIsAccepted() throws FriendshipRequestNotFound {
        boolean isAccepted = friendshipDao.isAccepted(1, 2);

        assertTrue(isAccepted);
    }
}
