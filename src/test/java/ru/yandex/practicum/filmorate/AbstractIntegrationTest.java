package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.initializer.Postgres;

@SpringBootTest
@AutoConfigureTestDatabase
@ContextConfiguration(initializers = {Postgres.Initializer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao-test-data.sql")
public abstract class AbstractIntegrationTest {
    @BeforeAll
    public static void inti() {
        Postgres.container.start();
    }
}
