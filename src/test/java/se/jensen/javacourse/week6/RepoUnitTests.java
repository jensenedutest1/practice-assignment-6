package se.jensen.javacourse.week6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.database.LibraryRepository;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RepoUnitTests
{
    @Autowired
    LibraryRepository repo;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @BeforeEach
    void beforeEach()
    {
        jdbcTemplate.update("""
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
                INSERT INTO artists (name) VALUES ('Led Zeppelin');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('Whole Lotta Love', 1969, 1);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('Immigrant Song', 1970, 1);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('Stairway to Heaven', 1971, 1);
                INSERT INTO artists (name) VALUES ('Pink Floyd');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('Wish You Were Here', 1975, 2);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('The Wall', 1979, 2);
                INSERT INTO artists (name) VALUES ('The Black Keys');
                INSERT INTO tracks (name, "year", artist_id) VALUES ('I Got Mine', 2008, 3);
                INSERT INTO tracks (name, "year", artist_id) VALUES ('Howlin'' for You', 2011, 3);""");
    }
    
    @AfterEach
    void afterEach()
    {
        jdbcTemplate.update("DROP TABLE IF EXISTS artists CASCADE; DROP TABLE IF EXISTS tracks;");
    }
    
    @Test
    public void readArtists()
    {
        HashMap<Integer, Artist> res = repo.readArtists();
        
        Artist artistOne = res.get(1);
        Track trackOne = artistOne.tracks.get(1);
        
        assertThat(res).hasSize(3);
        assertThat(artistOne).isNotNull();
        assertThat(artistOne.getName()).isEqualTo("Led Zeppelin");
        assertThat(artistOne.tracks).hasSize(3);
        assertThat(trackOne).isNotNull();
        assertThat(trackOne.getArtistId()).isEqualTo(1);
        assertThat(trackOne.getName()).isEqualTo("Whole Lotta Love");
    }
    
    @Test
    public void readArtistNames()
    {
        List<String> res = repo.readArtistNames();
    
        assertThat(res).isNotNull();
        assertThat(res).hasSize(3);
        assertThat(res).contains("Led Zeppelin");
    }
    
    @Test
    public void readArtistById()
    {
        Artist zero = repo.readArtistById(0);
        Artist one = repo.readArtistById(1);
        Artist three = repo.readArtistById(3);
        Artist ten = repo.readArtistById(10);
        assertThat(zero).isNull();
        
        assertThat(one).isNotNull();
        assertThat(one.getId()).isEqualTo(1);
        assertThat(one.getName()).isEqualTo("Led Zeppelin");
        
        assertThat(three).isNotNull();
        assertThat(three.getId()).isEqualTo(3);
        assertThat(three.getName()).isEqualTo("The Black Keys");
        
        assertThat(ten).isNull();
    }
    
    @Test
    public void readArtistByName()
    {
        Artist pinkFloyd = repo.readArtistByName("Pink Floyd");
        
        assertThat(repo.readArtistByName("Nonexistent")).isNull();
        assertThat(repo.readArtistByName(null)).isNull();
        assertThat(repo.readArtistByName("")).isNull();
        
        assertThat(pinkFloyd).isNotNull();
        assertThat(pinkFloyd.getId()).isEqualTo(2);
        assertThat(pinkFloyd.getName()).isEqualTo("Pink Floyd");
    }
    
    @Test
    public void insertArtist()
    {
        assertThat(repo.insertArtist("Led Zeppelin")).isEqualTo(-1);
        assertThat(repo.insertArtist("")).isEqualTo(-3);
        assertThat(repo.insertArtist(null)).isEqualTo(-3);
        assertThat(repo.insertArtist("Artist 4")).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(4);
        assertThat(names).contains("Artist 4");
    }
    
    @Test
    public void updateArtist()
    {
        assertThat(repo.updateArtist(4, new Artist(4, "Artist 4"))).isEqualTo(0);
        assertThat(repo.updateArtist(3, new Artist(3, ""))).isEqualTo(-3);
        assertThat(repo.updateArtist(3, new Artist(3, null))).isEqualTo(-3);
        assertThat(repo.updateArtist(3, new Artist(3, "Led Zeppelin"))).isEqualTo(-1);
        assertThat(repo.updateArtist(3, new Artist(3, "The White Keys"))).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(3);
        assertThat(names).contains("The White Keys");
    }
    
    @Test
    public void deleteArtist()
    {
        assertThat(repo.deleteArtist(0)).isEqualTo(0);
        assertThat(repo.deleteArtist(4)).isEqualTo(0);
        assertThat(repo.deleteArtist(1)).isEqualTo(1);
        assertThat(repo.deleteArtist(3)).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(1);
        assertThat(names).doesNotContain("The White Keys").doesNotContain("Led Zeppelin");
    }
    
    @Test
    public void readTracks()
    {
        List<Track> tracks = repo.readTracks();
        
        assertThat(tracks).isNotNull();
        assertThat(tracks).hasSize(7);
        
        Track trackOne = tracks.get(0);
        assertThat(trackOne).isNotNull();
        assertThat(trackOne.getArtistId()).isEqualTo(1);
        assertThat(trackOne.getName()).isEqualTo("Whole Lotta Love");
    }
    
    @Test
    public void readTrack()
    {
        assertThat(repo.readTrack(4, 1)).isNull();
        assertThat(repo.readTrack(1, 0)).isNull();
        Track track = repo.readTrack(2, 1);
        
        assertThat(track).isNotNull();
        assertThat(track.getId()).isEqualTo(2);
        assertThat(track.getArtistId()).isEqualTo(1);
        assertThat(track.getName()).isEqualTo("Immigrant Song");
        assertThat(track.getYear()).isEqualTo(1970);
    }
    
    @Test
    public void insertTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.insertTrack(1, new Track(4, null, 1969, 1)));
        
        int resUniqueNameAll = repo.insertTrack(1, new Track(4, "Ramble On", 1969, 1));
        int resUniqueNameForArtist = repo.insertTrack(1, new Track(4, "The Wall", 1979, 1));
        int resDuplicateName = repo.insertTrack(1, new Track(4, "Immigrant Song", 1969, 1));
        int resEmptyName = repo.insertTrack(1, new Track(4, "", 1969, 1));
        
        assertThat(resUniqueNameAll).isEqualTo(1);
        assertThat(resUniqueNameForArtist).isEqualTo(1);
        assertThat(resDuplicateName).isEqualTo(-1);
        assertThat(resEmptyName).isEqualTo(-4);
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (rs, i) -> rs.getString("name"));
        assertThat(trackNames).hasSize(9);
        assertThat(trackNames).contains("Ramble On");
        assertThat(trackNames).contains("The Wall");
    }
    
    @Test
    public void updateTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.updateTrack(1, 2, new Track(null, 1971)));
        
        int resUniqueNameAll = repo.updateTrack(1, 1, new Track("Ramble On", 1969));
        int resUniqueNameForArtist = repo.updateTrack(1, 2, new Track("The Wall", 1979));
        int resDuplicateName = repo.updateTrack(1, 2, new Track("Stairway to Heaven", 1971));
        int resEmptyName = repo.updateTrack(1, 2, new Track("", 1969));
        
        assertThat(resUniqueNameAll).isEqualTo(1);
        assertThat(resUniqueNameForArtist).isEqualTo(1);
        assertThat(resDuplicateName).isEqualTo(-1);
        assertThat(resEmptyName).isEqualTo(-3);
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (rs, i) -> rs.getString("name"));
        assertThat(trackNames).hasSize(7);
        assertThat(trackNames).contains("Ramble On");
        assertThat(trackNames).contains("The Wall");
        assertThat(trackNames).doesNotContain("Whole Lotta Love");
        assertThat(trackNames).doesNotContain("Immigrant Song");
    }
    
    @Test
    public void deleteTrack()
    {
        assertThat(repo.deleteTrack(0, 1)).isEqualTo(0);
        assertThat(repo.deleteTrack(4, 1)).isEqualTo(0);
        assertThat(repo.deleteTrack(1, 5)).isEqualTo(0);
        assertThat(repo.deleteTrack(1, 2)).isEqualTo(1);
    }
}
