//package se.jensen.javacourse.week6;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import se.jensen.javacourse.database.week6.LibraryRepository;
//import se.jensen.javacourse.model.week6.Artist;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//public class DBTests
//{
//    static int fakeArtistId = -10;
//    @Autowired LibraryRepository repo;
//
//    @BeforeAll
//    public static void createFakeArtist(@Autowired JdbcTemplate jdbcTemplate)
//    {
//        String sql = "INSERT INTO artists (name) VALUES ('Test Artist 2') RETURNING id";
//        RowMapper<Integer> rm = (rs, rn) -> rs.getInt("id");
//        fakeArtistId = jdbcTemplate.queryForObject(sql, rm);
//
//        System.out.println("newId = " + fakeArtistId);
//    }
//
//    @AfterAll
//    public static void deleteFakeArtist(@Autowired JdbcTemplate jdbcTemplate)
//    {
//        jdbcTemplate.update("DELETE FROM artists WHERE id = ?", fakeArtistId);
//    }
//
//    @Test
//    public void getTheFakeArtist() {
//        Artist foundArtist = repo.readArtistById(fakeArtistId);
//
//        System.out.println("foundArtist = " + foundArtist.getName());
//
//        assertNotNull(foundArtist);
//        Assertions.assertEquals(foundArtist.getId(), fakeArtistId);
//        Assertions.assertEquals(foundArtist.getName(), "Test Artist 2");
//    }
//}
//
//
//
//
//
//
//
