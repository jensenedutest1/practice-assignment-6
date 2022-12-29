package se.jensen.javacourse.week6.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

@Repository
public class LibraryRepository
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public HashMap<Integer, Artist> readArtists()
    {
        RowMapper<Artist> rmA = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        HashMap<Integer, Artist> artists = new HashMap<>();
        for (Artist a : jdbcTemplate.query("SELECT * FROM artists", rmA))
            artists.put(a.getId(), a);
        
        String sqlT = "SELECT * FROM tracks";
        RowMapper<Track> rmT = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        for (Track t : jdbcTemplate.query(sqlT, rmT))
            artists.get(t.getArtistId()).addTrack(t);
        return artists;
    }
    
    public List<String> readArtistNames()
    {
        String sql = "SELECT name FROM artists";
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        
        return jdbcTemplate.query(sql, rm);
    }
    
    public Artist readArtistById(int id)
    {
        String sql = "SELECT * FROM artists WHERE id = ?";
        
        RowMapper<Artist> rm = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        try
        {
            return jdbcTemplate.queryForObject(sql, rm, id);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
    public Artist readArtistByName(String name)
    {
        String sql = "SELECT * FROM artists WHERE name = ?";
        
        RowMapper<Artist> rm = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        try
        {
            return jdbcTemplate.queryForObject(sql, rm, name);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
    public int insertArtist(String name)
    {
        String sql = "INSERT INTO artists (name) VALUES (?)";
        try
        {
            return jdbcTemplate.update(sql, name);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("artists_name_key") || e.toString().contains("ARTISTS(NAME"))
                return -1;
        }
        return -3;
    }
    
    public int updateArtist(int id, Artist artist)
    {
        String sql = "UPDATE artists SET name = ? WHERE id = ?";
        try
        {
            return jdbcTemplate.update(sql, artist.getName(), id);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("artists_name_key") || e.toString().contains("ARTISTS(NAME"))
                return -1;
        }
        return -3;
    }
    
    public int deleteArtist(int id)
    {
        return jdbcTemplate.update("DELETE FROM artists WHERE id = ?", id);
    }
    
    public List<Track> readTracks()
    {
        String sql = "SELECT * FROM tracks";
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        return jdbcTemplate.query(sql, rm);
    }
    
    public Track readTrack(int trackId, int artistId)
    {
        String sql = "SELECT * FROM tracks WHERE id = ? AND artist_id = ?";
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        try
        {
            return jdbcTemplate.queryForObject(sql, rm, trackId, artistId);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
    
    public int insertTrack(int artistId, Track track)
    {
        String sql = "INSERT INTO tracks (name, \"year\", artist_id) VALUES (?, ?, ?)";
        try
        {
            return jdbcTemplate.update(sql, track.getName().trim(), track.getYear(), artistId);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("tracks_name_artist_id_key") || e.toString().contains("TRACKS(NAME"))
                return -1;
        }
        return -4;
    }
    
    public int updateTrack(int artistId, int trackId, Track track)
    {
        String sql = "UPDATE tracks SET name = ?, \"year\" = ? WHERE id = ? AND artist_id = ?";
        try
        {
            return jdbcTemplate.update(sql, track.getName().trim(), track.getYear(), trackId, artistId);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("tracks_name_artist_id_key") || e.toString().contains("TRACKS(NAME"))
                return -1;
        }
        return -4;
    }
    
    public int deleteTrack(int artistId, int trackId)
    {
        String sql = "DELETE FROM tracks WHERE id = ? AND artist_id = ?";
        return jdbcTemplate.update(sql, trackId, artistId);
    }
}
