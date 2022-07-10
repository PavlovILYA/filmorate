package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public class MpaDaoTest {
    private final MpaDao mpaDao;

    @Autowired
    public MpaDaoTest(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Test
    public void checkGet() {
        Mpa mpa = mpaDao.get(3);

        assertEquals(3, mpa.getId());
        assertEquals("PG-13", mpa.getName());
    }

    @Test
    public void checkGetAll() {
        List<Mpa> mpaList = mpaDao.getAll();

        assertEquals(5, mpaList.size());
    }
}
