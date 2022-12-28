package se.jensen.javacourse.week6.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** This class tests LibraryRepository using an actual database. */
public class RepoIntegrationTests extends ContextTests
{
    @Autowired
    LibraryRepository repo;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    final static int ART1_ID = 1;
    final static int ART2_ID = 2;
    final static int ART3_ID = 3;
    
    final static String ART1_NAME = "Led Zeppelin";
    final static String ART2_NAME = "Pink Floyd";
    final static String ART3_NAME = "The Black Keys";
    final static String NEW_ARTIST_NAME = "Greta Van Fleet";
    
    final static int TRK1_1_ID = 1;
    final static int TRK1_2_ID = 2;
    final static int TRK1_3_ID = 3;
    final static int TRK2_1_ID = 4;
    final static int TRK2_2_ID = 5;
    final static int TRK3_1_ID = 6;
    final static int TRK3_2_ID = 7;
    
    final static String TRK1_1_NAME = "Whole Lotta Love";
    final static String TRK1_2_NAME = "Immigrant Song";
    final static String TRK1_3_NAME = "Stairway to Heaven";
    final static String TRK2_1_NAME = "Wish You Were Here";
    final static String TRK2_2_NAME = "The Wall";
    final static String TRK3_1_NAME = "I Got Mine";
    final static String TRK3_2_NAME = "Howling for You";
    final static String NEW_TRACK_NAME = "Ramble On";
    
    final static int TRK1_1_YEAR = 1969;
    final static int TRK1_2_YEAR = 1970;
    final static int TRK1_3_YEAR = 1971;
    final static int TRK2_1_YEAR = 1975;
    final static int TRK2_2_YEAR = 1979;
    final static int TRK3_1_YEAR = 2008;
    final static int TRK3_2_YEAR = 2011;
    
    final static Artist ART_1 = new Artist(ART1_ID, ART1_NAME);
    final static Artist ART_2 = new Artist(ART2_ID, ART2_NAME);
    final static Artist ART_3 = new Artist(ART3_ID, ART3_NAME);
    
    final static Track TRK1_1 = new Track(TRK1_1_ID, TRK1_1_NAME, TRK1_1_YEAR, ART1_ID);
    final static Track TRK1_2 = new Track(TRK1_2_ID, TRK1_2_NAME, TRK1_2_YEAR, ART1_ID);
    final static Track TRK1_3 = new Track(TRK1_3_ID, TRK1_3_NAME, TRK1_3_YEAR, ART1_ID);
    final static Track TRK2_1 = new Track(TRK2_1_ID, TRK2_1_NAME, TRK2_1_YEAR, ART2_ID);
    final static Track TRK2_2 = new Track(TRK2_2_ID, TRK2_2_NAME, TRK2_2_YEAR, ART2_ID);
    final static Track TRK3_1 = new Track(TRK3_1_ID, TRK3_1_NAME, TRK3_1_YEAR, ART3_ID);
    final static Track TRK3_2 = new Track(TRK3_2_ID, TRK3_2_NAME, TRK3_2_YEAR, ART3_ID);
    
    final static List<Track> TRACKS = new ArrayList<>();
    
    @BeforeAll
    static void beforeAll()
    {
        ART_1.addTrack(TRK1_1);
        ART_1.addTrack(TRK1_2);
        ART_1.addTrack(TRK1_3);
        ART_2.addTrack(TRK2_1);
        ART_2.addTrack(TRK2_2);
        ART_3.addTrack(TRK3_1);
        ART_3.addTrack(TRK3_2);
        
        TRACKS.addAll(ART_1.tracks.values());
        TRACKS.addAll(ART_2.tracks.values());
        TRACKS.addAll(ART_3.tracks.values());
    }
    
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
    
    @Test
    public void readArtists()
    {
        HashMap<Integer, Artist> artistMap = new HashMap<>();
        artistMap.put(ART1_ID, ART_1);
        artistMap.put(ART2_ID, ART_2);
        artistMap.put(ART3_ID, ART_3);
        assertThat(repo.readArtists()).isEqualTo(artistMap);
    }
    
    @Test
    public void readArtistNames()
    {
        List<String> artistNames = new ArrayList<>();
        artistNames.add(ART1_NAME);
        artistNames.add(ART2_NAME);
        artistNames.add(ART3_NAME);
        
        assertThat(repo.readArtistNames()).isEqualTo(artistNames);
    }
    
    @Test
    public void readArtistById()
    {
        Artist artist1NoTracks = new Artist(ART1_ID, ART1_NAME);
        Artist artist2NoTracks = new Artist(ART2_ID, ART2_NAME);
        
        assertThat(repo.readArtistById(0)).isNull();
        assertThat(repo.readArtistById(10)).isNull();
        assertThat(repo.readArtistById(ART1_ID)).isEqualTo(artist1NoTracks);
        assertThat(repo.readArtistById(ART2_ID)).isEqualTo(artist2NoTracks);
    }
    
    @Test
    public void readArtistByName()
    {
        Artist artist1NoTracks = new Artist(ART1_ID, ART1_NAME);
        Artist artist2NoTracks = new Artist(ART2_ID, ART2_NAME);
        
        assertThat(repo.readArtistByName(null)).isNull();
        assertThat(repo.readArtistByName("")).isNull();
        assertThat(repo.readArtistByName("Nonexistent")).isNull();
        assertThat(repo.readArtistByName(ART1_NAME)).isEqualTo(artist1NoTracks);
        assertThat(repo.readArtistByName(ART2_NAME)).isEqualTo(artist2NoTracks);
    }
    
    @Test
    public void insertArtist()
    {
        assertThat(repo.insertArtist(ART1_NAME)).isEqualTo(-1);
        assertThat(repo.insertArtist("")).isEqualTo(-3);
        assertThat(repo.insertArtist(null)).isEqualTo(-3);
        assertThat(repo.insertArtist(NEW_ARTIST_NAME)).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(4);
        assertThat(names).contains(NEW_ARTIST_NAME);
    }
    
    @Test
    public void updateArtist()
    {
        assertThat(repo.updateArtist(4, new Artist(4, NEW_ARTIST_NAME))).isEqualTo(0);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, ""))).isEqualTo(-3);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, null))).isEqualTo(-3);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, ART2_NAME))).isEqualTo(-1);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, ART1_NAME))).isEqualTo(1);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, NEW_ARTIST_NAME))).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(3);
        assertThat(names).contains(NEW_ARTIST_NAME);
    }
    
    @Test
    public void deleteArtist()
    {
        assertThat(repo.deleteArtist(0)).isEqualTo(0);
        assertThat(repo.deleteArtist(4)).isEqualTo(0);
        assertThat(repo.deleteArtist(ART1_ID)).isEqualTo(1);
        assertThat(repo.deleteArtist(ART3_ID)).isEqualTo(1);
        
        List<String> names = jdbcTemplate.query("SELECT name FROM artists", (rs, i) -> rs.getString("name"));
        
        assertThat(names).isNotNull();
        assertThat(names).hasSize(1);
        assertThat(names).contains(ART2_NAME);
        assertThat(names).doesNotContain(ART3_NAME).doesNotContain(ART1_NAME);
    }
    
    @Test
    public void readTracks()
    {
        assertThat(repo.readTracks()).isEqualTo(TRACKS);
    }
    
    @Test
    public void readTrack()
    {
        assertThat(repo.readTrack(0, ART1_ID)).isNull();
        assertThat(repo.readTrack(TRK1_1_ID, 0)).isNull();
        assertThat(repo.readTrack(TRK1_1_ID, ART2_ID)).isNull();
        assertThat(repo.readTrack(TRK1_1_ID, ART1_ID)).isEqualTo(TRK1_1);
        assertThat(repo.readTrack(TRK1_3_ID, ART1_ID)).isEqualTo(TRK1_3);
        assertThat(repo.readTrack(TRK2_2_ID, ART2_ID)).isEqualTo(TRK2_2);
    }
    
    @Test
    public void insertTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.insertTrack(ART1_ID, new Track(4, null, 1969, 1)));
        
        Track trackDuplicateName = new Track(8, TRK1_1_NAME, 2022, ART1_ID);
        Track trackEmptyName = new Track(9, "", 2022, ART1_ID);
        Track trackNewName = new Track(10, NEW_TRACK_NAME, 2022, ART1_ID);
        Track trackUniqueForArtist = new Track(11, TRK2_1_NAME, 2022, ART1_ID);
        
        int resDuplicateName = repo.insertTrack(ART1_ID, trackDuplicateName);
        int resEmptyName = repo.insertTrack(ART1_ID, trackEmptyName);
        int resUniqueNameAll = repo.insertTrack(ART1_ID, trackNewName);
        int resUniqueNameForArtist = repo.insertTrack(ART1_ID, trackUniqueForArtist);
        
        assertThat(resDuplicateName).isEqualTo(-1);
        assertThat(resEmptyName).isEqualTo(-4);
        assertThat(resUniqueNameAll).isEqualTo(1);
        assertThat(resUniqueNameForArtist).isEqualTo(1);
        
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        List<Track> tracks = jdbcTemplate.query("SELECT * FROM tracks", rm);
        
        assertThat(tracks).hasSize(9);
        assertThat(tracks).contains(trackNewName);
        assertThat(tracks).contains(trackUniqueForArtist);
        assertThat(tracks).doesNotContain(trackDuplicateName);
        assertThat(tracks).doesNotContain(trackEmptyName);
    }
    
    @Test
    public void updateTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.updateTrack(ART1_ID, TRK1_2_ID, new Track(null, 1971)));
        
        Track trackDuplicateName = new Track(TRK1_2_ID, TRK1_1_NAME, 2022, ART1_ID);
        Track trackNewName = new Track(TRK1_1_ID, NEW_TRACK_NAME, 2022, ART1_ID);
        Track trackUniqueForArtist = new Track(TRK1_2_ID, TRK2_1_NAME, 2022, ART1_ID);
        Track trackEmptyName = new Track(TRK1_2_ID, "", 2022, ART1_ID);
        
        int resDuplicateName = repo.updateTrack(ART1_ID, TRK1_2_ID, trackDuplicateName);
        int resUniqueNameAll = repo.updateTrack(ART1_ID, TRK1_1_ID, trackNewName);
        int resUniqueNameForArtist = repo.updateTrack(ART1_ID, TRK1_2_ID, trackUniqueForArtist);
        int resEmptyName = repo.updateTrack(ART1_ID, TRK1_2_ID, trackEmptyName);
        
        assertThat(resDuplicateName).isEqualTo(-1);
        assertThat(resUniqueNameAll).isEqualTo(1);
        assertThat(resUniqueNameForArtist).isEqualTo(1);
        assertThat(resEmptyName).isEqualTo(-4);
        
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        List<Track> tracks = jdbcTemplate.query("SELECT * FROM tracks", rm);
        
        assertThat(tracks).hasSize(7);
        assertThat(tracks).contains(trackNewName);
        assertThat(tracks).contains(trackUniqueForArtist);
        assertThat(tracks).doesNotContain(trackDuplicateName);
        assertThat(tracks).doesNotContain(trackEmptyName);
    }
    
    @Test
    public void deleteTrack()
    {
        assertThat(repo.deleteTrack(0, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(0, TRK1_1_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(4, TRK1_1_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART2_ID, TRK1_1_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, 5)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, TRK1_2_ID)).isEqualTo(1);
    }
}
