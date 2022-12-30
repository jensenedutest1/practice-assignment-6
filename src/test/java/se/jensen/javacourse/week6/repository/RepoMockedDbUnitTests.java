package se.jensen.javacourse.week6.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.jensen.javacourse.week6.Tests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

/** This class mocks JdbcTemplate and tests only the bare LibraryRepository functions. */
public class RepoMockedDbUnitTests extends Tests
{
    static LibraryRepository repo;
    
    @BeforeAll
    public static void beforeAll() throws NoSuchFieldException, IllegalAccessException
    {
        Tests.beforeAll();
        
        final List<Artist> artistList = new ArrayList<>();
        artistList.add(ART_1);
        artistList.add(ART_2);
        artistList.add(ART_3);
        
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), any(RowMapper.class))).thenReturn(ARTIST_NAMES);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM artists"), any(RowMapper.class))).thenReturn(artistList);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM tracks"), any(RowMapper.class))).thenReturn(TRACKS);
        Mockito.when(jdbcTemplate.query(eq("SELECT name FROM artists"), any(RowMapper.class))).thenReturn(ARTIST_NAMES);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE id = ?"),
                                                 any(RowMapper.class), eq(ART1_ID))).thenReturn(ART_1);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE id = ?"),
                                                 any(RowMapper.class), eq(ART2_ID))).thenReturn(ART_2);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM artists WHERE name = ?"),
                                                 any(RowMapper.class), eq(ART1_NAME))).thenReturn(ART_1);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", ART1_NAME)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", NAME_DUPLICATE)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", "")).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("INSERT INTO artists (name) VALUES (?)", (String) null)).thenReturn(-3);
        Mockito.when(jdbcTemplate.update(eq("UPDATE artists SET name = ? WHERE id = ?"), eq(""), anyInt())).thenReturn(-3);
        Mockito.when(jdbcTemplate.update(eq("UPDATE artists SET name = ? WHERE id = ?"), isNull(), anyInt())).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("UPDATE artists SET name = ? WHERE id = ?", NAME_DUPLICATE, ART1_ID)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("UPDATE artists SET name = ? WHERE id = ?", ART1_NAME, ART1_ID)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 1)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 2)).thenReturn(1);
        Mockito.when(jdbcTemplate.query(eq("SELECT * FROM tracks"), any(RowMapper.class))).thenReturn(TRACKS);
        Mockito.when(jdbcTemplate.queryForObject(eq("SELECT * FROM tracks WHERE id = ? AND artist_id = ?"),
                                                 any(RowMapper.class), eq(TRK1_1_ID), eq(ART1_ID))).thenReturn(TRK1_1);
        Mockito.when(jdbcTemplate.update(eq("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)"),
                                         isNull(), anyInt(), anyInt())).thenThrow(NullPointerException.class);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         TRK1_1_NAME, TRK1_1_YEAR, ART1_ID)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         NAME_DUPLICATE, TRK1_1_YEAR, ART1_ID)).thenReturn(-1);
        Mockito.when(jdbcTemplate.update("INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)",
                                         "", TRK1_1_YEAR, ART1_ID)).thenReturn(-4);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         isNull(), anyInt(), anyInt(), anyInt())).thenThrow(NullPointerException.class);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(TRK1_2_NAME), anyInt(), eq(TRK1_1_ID), eq(ART1_ID))).thenReturn(1);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(NAME_DUPLICATE), anyInt(), eq(TRK1_1_ID), eq(ART1_ID))).thenReturn(-1);
        Mockito.when(jdbcTemplate.update(eq("UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?"),
                                         eq(""), anyInt(), eq(TRK1_1_ID), eq(ART1_ID))).thenReturn(-3);
        Mockito.when(jdbcTemplate.update("DELETE FROM artists WHERE id = ?", 1)).thenReturn(1);
        Mockito.when(jdbcTemplate.update("DELETE FROM tracks WHERE id = ? AND artist_id = ?",
                                         TRK1_1_ID, ART1_ID)).thenReturn(1);
        
        repo = new LibraryRepository();
        Field field = repo.getClass().getDeclaredField("jdbcTemplate");
        field.setAccessible(true);
        field.set(repo, jdbcTemplate);
    }
    
    @Test
    public void readArtists()
    {
        assertThat(repo.readArtists()).isEqualTo(ARTISTS_MAP);
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
        assertThat(repo.readArtistById(9)).isNull();
        assertThat(repo.readArtistById(ART1_ID)).isEqualTo(ART_1);
        assertThat(repo.readArtistById(ART2_ID)).isEqualTo(ART_2);
    }
    
    @Test
    public void readArtistByName()
    {
        assertThat(repo.readArtistByName(ART1_NAME)).isEqualTo(ART_1);
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
        assertThat(repo.insertArtist(ART1_NAME)).isEqualTo(1);
        }
    
    @Test
    public void updateArtist()
    {
        // non-existent id updates 0 rows
        assertThat(repo.updateArtist(9, new Artist(9, "Whatever"))).isEqualTo(0);
        // correct id and empty or null name returns -3
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, ""))).isEqualTo(-3);
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, null))).isEqualTo(-3);
        // correct id and duplicate name returns -1
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, NAME_DUPLICATE))).isEqualTo(-1);
        // correct id and valid name updates 1 row
        assertThat(repo.updateArtist(ART1_ID, new Artist(ART1_ID, ART1_NAME))).isEqualTo(1);
    }
    
    @Test
    public void deleteArtist()
    {
        assertThat(repo.deleteArtist(0)).isEqualTo(0);
        assertThat(repo.deleteArtist(9)).isEqualTo(0);
        assertThat(repo.deleteArtist(ART1_ID)).isEqualTo(1);
        assertThat(repo.deleteArtist(ART2_ID)).isEqualTo(1);
    }
    
    @Test
    public void readTracks()
    {
        assertThat(repo.readTracks()).isEqualTo(TRACKS);
    }
    
    @Test
    public void readTrack()
    {
        assertThat(repo.readTrack(9, 9)).isNull();
        assertThat(repo.readTrack(0, ART1_ID)).isNull();
        assertThat(repo.readTrack(TRK1_1_ID, 0)).isNull();
        assertThat(repo.readTrack(TRK1_1_ID, ART1_ID)).isEqualTo(TRK1_1);
    }
    
    @Test
    public void insertTrack()
    {
        assertThrows(NullPointerException.class, () ->
                repo.insertTrack(ART1_ID, new Track(TRK1_1_ID, null, TRK1_1_YEAR, ART1_ID)));
        
        assertThat(repo.insertTrack(0, TRK1_1)).isEqualTo(0);
        assertThat(repo.insertTrack(ART1_ID, TRK1_1)).isEqualTo(1);
        assertThat(repo.insertTrack(ART1_ID, new Track(TRK1_1_ID, NAME_DUPLICATE, TRK1_1_YEAR, ART1_ID))).isEqualTo(-1);
        assertThat(repo.insertTrack(ART1_ID, new Track(TRK1_1_ID, "", TRK1_1_YEAR, ART1_ID))).isEqualTo(-4);
    }
    
    @Test
    public void updateTrack()
    {
        assertThrows(NullPointerException.class, () -> repo.updateTrack(ART1_ID, TRK1_1_ID, new Track(null, TRK1_1_YEAR)));
        assertThat(repo.updateTrack(0, TRK1_1_ID, new Track(TRK1_2_NAME, TRK1_1_YEAR))).isEqualTo(0);
        assertThat(repo.updateTrack(ART1_ID, 0, new Track(TRK1_2_NAME, TRK1_1_YEAR))).isEqualTo(0);
        assertThat(repo.updateTrack(ART1_ID, TRK1_1_ID, new Track(TRK1_2_NAME, TRK1_1_YEAR))).isEqualTo(1);
        assertThat(repo.updateTrack(ART1_ID, TRK1_1_ID, new Track(NAME_DUPLICATE, TRK1_1_YEAR))).isEqualTo(-1);
        assertThat(repo.updateTrack(ART1_ID, TRK1_1_ID, new Track("", TRK1_1_YEAR))).isEqualTo(-3);
        }
    
    @Test
    public void deleteTrack()
    {
        assertThat(repo.deleteTrack(0, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(0, TRK1_1_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(9, TRK1_1_ID)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, 0)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, 9)).isEqualTo(0);
        assertThat(repo.deleteTrack(ART1_ID, TRK1_1_ID)).isEqualTo(1);
    }
}
