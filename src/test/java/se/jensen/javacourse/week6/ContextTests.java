package se.jensen.javacourse.week6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * This class starts a server and a Spring context on a random port.
 * It also sets up the db tables and rows before EACH test method and
 * tears them down after EACH test method.
 * Some test classes inherit from this class, which means they also inherit
 * the Spring context and the setup and tear-down of the database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContextTests extends Tests
{
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    @BeforeEach
    void beforeEach()
    {
        String sql = """
                CREATE TABLE artists (
                	id SERIAL PRIMARY KEY,
                	name TEXT NOT NULL UNIQUE,
                	CHECK (name <> '')
                );
                CREATE TABLE tracks (
                	id SERIAL PRIMARY KEY,
                	name TEXT NOT NULL,
                    CHECK (name <> ''),
                	"year" INT,
                	artist_id INT,
                	UNIQUE (name, artist_id),
                	FOREIGN KEY (artist_id)
                	REFERENCES artists (id)
                	ON DELETE CASCADE
                );
                INSERT INTO artists (name) VALUES ('%s');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO artists (name) VALUES ('%s');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO artists (name) VALUES ('%s');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('%s', %d, %d);"""
                .formatted(ART1_NAME, TRK1_1_NAME, TRK1_1_YEAR, ART1_ID, TRK1_2_NAME, TRK1_2_YEAR, ART1_ID,
                           TRK1_3_NAME, TRK1_3_YEAR, ART1_ID,
                           ART2_NAME, TRK2_1_NAME, TRK2_1_YEAR, ART2_ID, TRK2_2_NAME, TRK2_2_YEAR, ART2_ID,
                           ART3_NAME, TRK3_1_NAME, TRK3_1_YEAR, ART3_ID, TRK3_2_NAME, TRK3_2_YEAR, ART3_ID);
        jdbcTemplate.update(sql);
    }
    
    @AfterEach
    void afterEach()
    {
        jdbcTemplate.update("DROP TABLE IF EXISTS artists CASCADE; DROP TABLE IF EXISTS tracks;");
    }
}
