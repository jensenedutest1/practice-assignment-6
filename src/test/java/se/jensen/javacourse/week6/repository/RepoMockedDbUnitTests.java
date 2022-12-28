package se.jensen.javacourse.week6.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

/** This class mocks JdbcTemplate and tests only the bare LibraryRepository functions. */
public class RepoMockedDbUnitTests
{
    static LibraryRepository repo;
    
    final static String ARTIST_NAME = "Artist";
    final static String ARTIST_NAME_TWO = "Artist 2";
    final static String NAME_DUPLICATE = "Duplicate";
    final static int ARTIST_ID = 1;
    final static int ARTIST_ID_TWO = 2;
    final static Artist ARTIST_NOMINAL = new Artist(ARTIST_ID, ARTIST_NAME);
    final static Artist ARTIST_NOMINAL_TWO = new Artist(ARTIST_ID_TWO, ARTIST_NAME_TWO);
    final static HashMap<Integer, Artist> ARTISTS = new HashMap<>();
    final static List<String> ARTIST_NAMES = new ArrayList<>();
    final static int TRACK_YEAR = 2000;
    final static int TRACK_ID = 3;
    final static String TRACK_NAME = "Track";
    final static String TRACK_NAME_TWO = "Track 2";
    final static Track TRACK_NOMINAL = new Track(TRACK_ID, TRACK_NAME, TRACK_YEAR, ARTIST_ID);
    final static List<Track> TRACK_LIST = new ArrayList<>();
    
    @BeforeAll
    public static void beforeAll() throws NoSuchFieldException, IllegalAccessException
    {
        ARTIST_NAMES.add(ARTIST_NAME);
        ARTIST_NAMES.add(ARTIST_NAME_TWO);
        ARTISTS.put(ARTIST_ID, ARTIST_NOMINAL);
        final List<Artist> artistList = new ArrayList<>();
        artistList.add(ARTIST_NOMINAL);
        artistList.add(ARTIST_NOMINAL_TWO);
        ARTIST_NOMINAL.addTrack(TRACK_NOMINAL);
        ARTISTS.put(ARTIST_ID, ARTIST_NOMINAL);
        ARTISTS.put(ARTIST_ID_TWO, ARTIST_NOMINAL_TWO);
        TRACK_LIST.add(TRACK_NOMINAL);
        
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), any(RowMapper.class))).thenReturn(ARTIST_NAMES);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM artists"), any(RowMapper.class))).thenReturn(artistList);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM tracks"), any(RowMapper.class))).thenReturn(TRACK_LIST);
        Mockito.when(jdbcTemplate.query(eq("SELECT name FROM artists"), any(RowMapper.class))).thenReturn(ARTIST_NAMES);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE id = ?"),
                                                 any(RowMapper.class), eq(ARTIST_ID))).thenReturn(ARTIST_NOMINAL);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE id = ?"),
                                                 any(RowMapper.class), eq(ARTIST_ID_TWO))).thenReturn(ARTIST_NOMINAL_TWO);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE name = ?"),
                                                 any(RowMapper.class), eq(ARTIST_NAME))).thenReturn(ARTIST_NOMINAL);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", ARTIST_NAME)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", NAME_DUPLICATE)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", "")).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", (String) null)).thenReturn(-3);
        Mockito.when(jdbcTemplate.update(eq("UPDATE artists SET name = ? WHERE id = ?"), eq(""), anyInt())).thenReturn(-3);
        Mockito.when(jdbcTemplate.update(eq("UPDATE artists SET name = ? WHERE id = ?"), isNull(), anyInt())).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("UPDATE artists SET name = ? WHERE id = ?", NAME_DUPLICATE, ARTIST_ID)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("UPDATE artists SET name = ? WHERE id = ?", ARTIST_NAME, ARTIST_ID)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 1)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 2)).thenReturn(1);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM tracks"), any(RowMapper.class))).thenReturn(TRACK_LIST);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM tracks WHERE id = ? AND artist_id = ?"),
                                                 any(RowMapper.class), eq(TRACK_ID), eq(ARTIST_ID))).thenReturn(TRACK_NOMINAL);
        Mockito.when(jdbcTemplate.update(eq("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)"),
                                         isNull(), anyInt(), anyInt())).thenThrow(NullPointerException.class);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         TRACK_NAME, TRACK_YEAR, ARTIST_ID)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         NAME_DUPLICATE, TRACK_YEAR, ARTIST_ID)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         "", TRACK_YEAR, ARTIST_ID)).thenReturn(-4);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         isNull(), anyInt(), anyInt(), anyInt())).thenThrow(NullPointerException.class);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(TRACK_NAME_TWO), anyInt(), eq(TRACK_ID), eq(ARTIST_ID))).thenReturn(1);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(NAME_DUPLICATE), anyInt(), eq(TRACK_ID), eq(ARTIST_ID))).thenReturn(-1);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(""), anyInt(), eq(TRACK_ID), eq(ARTIST_ID))).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 1)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM tracks WHERE id = ? AND artist_id = ?",
                                         TRACK_ID, ARTIST_ID)).thenReturn(1);
        
        repo = new LibraryRepository();
        Field field = repo.getClass().getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(repo, jdbcTemplate);
    }
    
    @Test
    public void readArtists()
    {
        assertThat(repo.readArtists()).isEqualTo(ARTISTS);
    }
    
    @Test
    public void readArtistNames()
    {
        assertThat(repo.readArtistNames()).isEqualTo(ARTIST_NAMES);
    }
    
    @Test
    public void readArtistById()
    {
        assertThat(repo.readArtistById(0)).isNull();
        assertThat(repo.readArtistById(10)).isNull();
        assertThat(repo.readArtistById(ARTIST_ID)).isEqualTo(ARTIST_NOMINAL);
        assertThat(repo.readArtistById(ARTIST_ID_TWO)).isEqualTo(ARTIST_NOMINAL_TWO);
    }
    
    @Test
    public void readArtistByName()
    {
        assertThat(repo.readArtistByName(ARTIST_NAME)).isEqualTo(ARTIST_NOMINAL);
        assertThat(repo.readArtistByName("Nonexistent")).isNull();
        assertThat(repo.readArtistByName(null)).isNull();
        assertThat(repo.readArtistByName("")).isNull();
        
    }
    
    @Test
    public void insertArtist()
    {
        assertThat(repo.insertArtist(NAME_DUPLICATE)).isEqualTo(-1);
        assertThat(repo.insertArtist("")).isEqualTo(-3);
        assertThat(repo.insertArtist(null)).isEqualTo(-3);
        assertThat(repo.insertArtist(ARTIST_NAME)).isEqualTo(1);
        }
    
    @Test
    public void updateArtist()
    {
        // non-existent id updates 0 rows
        assertThat(repo.updateArtist(4, new Artist(4, "Whatever"))).isEqualTo(0);
        // correct id and empty or null name returns -3
        assertThat(repo.updateArtist(ARTIST_ID, new Artist(ARTIST_ID, ""))).isEqualTo(-3);
        assertThat(repo.updateArtist(ARTIST_ID, new Artist(ARTIST_ID, null))).isEqualTo(-3);
        // correct id and duplicate name returns -1
        assertThat(repo.updateArtist(ARTIST_ID, new Artist(ARTIST_ID, NAME_DUPLICATE))).isEqualTo(-1);
        // correct id and valid name updates 1 row
        assertThat(repo.updateArtist(ARTIST_ID, new Artist(ARTIST_ID, ARTIST_NAME))).isEqualTo(1);
    }
    
    @Test
    public void deleteArtist()
    {
        assertThat(repo.deleteArtist(0)).isEqualTo(0);
        assertThat(repo.deleteArtist(4)).isEqualTo(0);
        assertThat(repo.deleteArtist(ARTIST_ID)).isEqualTo(1);
        assertThat(repo.deleteArtist(ARTIST_ID_TWO)).isEqualTo(1);
    }
    
    @Test
    public void readTracks()
    {
        assertThat(repo.readTracks()).isEqualTo(TRACK_LIST);
    }
    
    @Test
    public void readTrack()
    {
        assertThat(repo.readTrack(4, 4)).isNull();
        assertThat(repo.readTrack(0, ARTIST_ID)).isNull();
        assertThat(repo.readTrack(TRACK_ID, 0)).isNull();
        assertThat(repo.readTrack(TRACK_ID, ARTIST_ID)).isEqualTo(TRACK_NOMINAL);
    }
    
    @Test
    public void insertTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.insertTrack(ARTIST_ID, new Track(TRACK_ID, null, TRACK_YEAR, ARTIST_ID)));
        
        assertThat(repo.insertTrack(0, TRACK_NOMINAL)).isEqualTo(0);
        assertThat(repo.insertTrack(ARTIST_ID, TRACK_NOMINAL)).isEqualTo(1);
        assertThat(repo.insertTrack(ARTIST_ID, new Track(TRACK_ID, NAME_DUPLICATE, TRACK_YEAR, ARTIST_ID))).isEqualTo(-1);
        assertThat(repo.insertTrack(ARTIST_ID, new Track(TRACK_ID, "", TRACK_YEAR, ARTIST_ID))).isEqualTo(-4);
        }
    
    @Test
    public void updateTrack()
    {
        assertThrows(NullPointerException.class, () -> repo.updateTrack(ARTIST_ID, TRACK_ID, new Track(null, TRACK_YEAR)));
        assertThat(repo.updateTrack(0, TRACK_ID, new Track(TRACK_NAME_TWO, TRACK_YEAR))).isEqualTo(0);
        assertThat(repo.updateTrack(ARTIST_ID, 0, new Track(TRACK_NAME_TWO, TRACK_YEAR))).isEqualTo(0);
        assertThat(repo.updateTrack(ARTIST_ID, TRACK_ID, new Track(TRACK_NAME_TWO, TRACK_YEAR))).isEqualTo(1);
        assertThat(repo.updateTrack(ARTIST_ID, TRACK_ID, new Track(NAME_DUPLICATE, TRACK_YEAR))).isEqualTo(-1);
        assertThat(repo.updateTrack(ARTIST_ID, TRACK_ID, new Track("", TRACK_YEAR))).isEqualTo(-3);
        }
    
    @Test
    public void deleteTrack()
    {
        assertThat(repo.deleteTrack(0, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(0, TRACK_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(2, TRACK_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(ARTIST_ID, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(ARTIST_ID, 2)).isEqualTo(0);
        assertThat(repo.deleteTrack(ARTIST_ID, TRACK_ID)).isEqualTo(1);
    }
}
