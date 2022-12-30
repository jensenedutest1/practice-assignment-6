package se.jensen.javacourse.week6.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** This class tests LibraryRepository using an actual H2 database. */
public class RepoIntegrationTests extends ContextTests
{
    @Autowired
    protected LibraryRepository repo;
    
    @Test
    public void readArtists()
    {
        ARTISTS_MAP.put(ART1_ID, ART_1);
        ARTISTS_MAP.put(ART2_ID, ART_2);
        ARTISTS_MAP.put(ART3_ID, ART_3);
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
        assertThat(repo.readArtistById(10)).isNull();
        assertThat(repo.readArtistById(ART1_ID)).isEqualTo(ARTIST_1_NO_TRACKS);
        assertThat(repo.readArtistById(ART2_ID)).isEqualTo(ARTIST_2_NO_TRACKS);
    }
    
    @Test
    public void readArtistByName()
    {
        assertThat(repo.readArtistByName(null)).isNull();
        assertThat(repo.readArtistByName("")).isNull();
        assertThat(repo.readArtistByName("Nonexistent")).isNull();
        assertThat(repo.readArtistByName(ART1_NAME)).isEqualTo(ARTIST_1_NO_TRACKS);
        assertThat(repo.readArtistByName(ART2_NAME)).isEqualTo(ARTIST_2_NO_TRACKS);
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
