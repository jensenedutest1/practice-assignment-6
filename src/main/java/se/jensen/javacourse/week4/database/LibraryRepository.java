package se.jensen.javacourse.week4.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week4.model.Artist;
import se.jensen.javacourse.week4.model.Track;

@Repository
public class LibraryRepository
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public List<String> readArtistNames()
    {
        // NOTE: this method is only supposed to read the names
        //  of all artists and return a list of Strings (the names)
        
        // todo: write an SQL query that selects only
        //  the name column from the artists table
        String sql = "SELECT name FROM artists";
        
        // you can use this RowMapper, no need to change it
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        
        // todo: execute the query and return whatever it returns
        return jdbcTemplate.query(sql, rm);
    }
    
    public HashMap<Integer, Artist> readArtists()
    {
        // NOTE: this method is complete, no need to touch it
        
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
    
    public Artist readArtistById(int id)
    {
        // NOTE: this method is supposed to get one artist with
        //  all their details and return an Artist object
        
        // todo: write an SQL query that selects all columns from the artists
        //  table, but only for rows where id equals the method parameter 'id'
        String sql = "SELECT * FROM artists WHERE id = ?";
        
        // todo: create a RowMapper that uses the
        //  constructor Artist(int id, String name)
        RowMapper<Artist> rm = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        try
        {
            // leave this as it is
            return jdbcTemplate.queryForObject(sql, rm, id);
        }
        catch (EmptyResultDataAccessException e)
        {
            // leave this as it is
            return null;
        }
    }
    
    public Artist readArtistByName(String name)
    {
        // todo: write an SQL query that selects all columns from the artists table,
        //  but only for rows where name equals the method parameter 'name'
        String sql = "SELECT * FROM artists WHERE name = ?";
        
        // todo: create a RowMapper that uses the constructor Artist(int id, String name)
        RowMapper<Artist> rm = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        try
        {
            // leave this as it is
            return jdbcTemplate.queryForObject(sql, rm, name);
        }
        catch (EmptyResultDataAccessException e)
        {
            // leave this as it is
            return null;
        }
    }
    
    public int insertArtist(String name)
    {
        // todo: write an SQL query that inserts a new row into
        //  the artists table, specifying the artist's name
        String sql = "INSERT INTO artists (name) VALUES (?)";
        try
        {
            // todo: execute the query and return whatever it returns
            return jdbcTemplate.update(sql, name);
        }
        catch (DataIntegrityViolationException e)
        {
            // leave this as it is
            if (e.toString().contains("artists_name_key"))
                return -1;
        }
        // leave this as it is
        return -3;
    }
    
    public int updateArtist(int id, Artist artist)
    {
        // todo: write an SQL query that updates rows in the artists table
        //  where the 'id' column equals the method parameter 'id'
        //  and set the 'name' column to the method parameter 'name'
        String sql = "UPDATE artists SET name = ? WHERE id = ?";
        try
        {
            // todo: execute the query and return its return value
            // hint: the new 'name' value is inside the 'artist' object,
            //  so get it from there
            return jdbcTemplate.update(sql, artist.getName(), id);
        }
        catch (DataIntegrityViolationException e)
        {
            // leave this as it is
            if (e.toString().contains("artists_name_key"))
                return -1;
        }
        return -3;
    }
    
    public int insertTrack(int artistId, Track track)
    {
        // todo: write an SQL query that inserts a new row into the tracks table,
        //  specifying values for the following three columns: name, year, and artist_id
        String sql = "INSERT INTO tracks (name, year, artist_id) VALUES (?, ?, ?)";
        try
        {
            return jdbcTemplate.update(sql, track.getName().trim(), track.getYear(), artistId);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("tracks_name_artist_id_key"))
                return -1;
        }
        return -4;
    }
    
    public int deleteArtist(int id)
    {
        return jdbcTemplate.update("DELETE FROM artists WHERE id = ?", id);
    }
    
    public int updateTrack(int artistId, int trackId, Track track)
    {
        String sql = "UPDATE tracks SET name = ?, year = ? WHERE id = ? AND artist_id = ?";
        try
        {
            return jdbcTemplate.update(sql, track.getName(), track.getYear(), trackId, artistId);
        }
        catch (DataIntegrityViolationException e)
        {
            if (e.toString().contains("tracks_name_artist_id_key"))
                return -1;
        }
        return -3;
    }
    
    public int deleteTrack(int artistId, int trackId)
    {
        String sql = "DELETE FROM tracks WHERE id = ? AND artist_id = ?";
        return jdbcTemplate.update(sql, trackId, artistId);
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
}
