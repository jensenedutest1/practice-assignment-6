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

public class LibraryRepository
{
    public List<String> readArtistNames()
    {
        // NOTE: this method is only supposed to read the names
        //  of all artists and return a list of Strings (the names)
        
        // todo: write an SQL query that selects only
        //  the name column from the artists table
        String sql = "";// <<< todo: SQL query here
        
        // you can use this RowMapper, no need to change it
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        
        // todo: execute the query and return whatever it returns
        return null;// <<< todo: execute the query here and return the result
    }
    
    public HashMap<Integer, Artist> readArtists()
    {
        // NOTE: this method is complete, no need to touch it
    
        // leave this as it is
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
        String sql = "";// <<< todo: SQL query here
        
        // todo: create a RowMapper that uses the
        //  constructor Artist(int id, String name)
//        RowMapper<Artist> rm = (rs, rowNum) -> ..... <<< todo: CONTINUE HERE
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
        String sql = "";// <<< todo: SQL query here
        
        // todo: create a RowMapper that uses the constructor Artist(int id, String name)
//        RowMapper<Artist> rm = (rs, rowNum) -> ..... <<< todo: CONTINUE HERE
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
        String sql = "";// <<< todo: SQL query here
        try
        {
            // todo: execute the query and return whatever it returns
            return 0;// <<< todo: execute the query here and return the result
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
        String sql = "";// <<< todo: SQL query here
        try
        {
            // todo: execute the query and return its return value
            // hint: the new 'name' value is inside the 'artist' object,
            //  so get it from there
            return 0;// <<< todo: execute the query here and return the result
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
    
    public int insertTrack(int artistId, Track track)
    {
        // todo: write an SQL query that inserts a new row into the tracks table,
        //  specifying values for the following three columns: name, year, and artist_id
        String sql = "";// <<< todo: SQL query here
        try
        {
            // leave this as it is
            return jdbcTemplate.update(sql, track.getName().trim(), track.getYear(), artistId);
        }
        catch (DataIntegrityViolationException e)
        {
            // leave this as it is
            if (e.toString().contains("tracks_name_artist_id_key"))
                return -1;
        }
        // leave this as it is
        return -4;
    }
    
    public int deleteArtist(int id)
    {
        // todo: write an SQL query that deletes a row from artists that has the passed id
        return jdbcTemplate.update(" >>> SQL QUERY HERE <<< ", id);
    }
    
    public int updateTrack(int artistId, int trackId, Track track)
    {
        String sql = "UPDATE tracks SET name = ?, year = ? WHERE id = ? AND artist_id = ?";
        try
        {
            // todo: execute the query and pass the appropriate variables for the question marks in the SQL
            return 0;// <<< todo: execute the query here and return the result
        }
        catch (DataIntegrityViolationException e)
        {
            // leave this as it is
            if (e.toString().contains("tracks_name_artist_id_key"))
                return -1;
        }
        // leave this as it is
        return -3;
    }
    
    public int deleteTrack(int artistId, int trackId)
    {
        // todo: write an SQL query that deletes a row from tracks
        //  that has a specific id and artist_id
        // note: be careful, the column names are id and artist_id,
        //  whereas the Java variables are trackId and artistId, respectively!
        String sql = "DELETE FROM tracks WHERE id = ? AND artist_id = ?";
        
        // todo: execute the query and pass the appropriate variables
        // note: the order of the variables is very important
        return 0;// <<< todo: execute the query here and return the result
    }
    
    public List<Track> readTracks()
    {
        // todo: write an SQL query that gets everything from the tracks table
        String sql = "";// <<< todo: SQL query here
    
        
        // todo: create a RowMapper that uses the constructor
        //  Track(int id, String name, int year, int artistId)
        // note: IMPORTANT: the column names are: id, name, year, artist_id
        //  when you use rs to get the values, you need to pass the column name as the method parameter
//        RowMapper<Track> rm = (rs, rowNum) -> ..... <<< todo: CONTINUE HERE
        
        
        // leave this as it is
        return jdbcTemplate.query(sql, rm);
    }
    
    public Track readTrack(int trackId, int artistId)
    {
        // todo: write an SQL query that gets a track with a specific id and a specific artist_id
        String sql = "";// <<< todo: SQL query here
        
        
        // todo: create a RowMapper that uses the constructor
        //  Track(int id, String name, int year, int artistId)
        // note: IMPORTANT: the column names are: id, name, year, artist_id
        //  when you use rs to get the values, you need to pass the column name as the method parameter
//        RowMapper<Track> rm = (rs, rowNum) -> ..... <<< todo: CONTINUE HERE
        
        
        // leave everything else as it is
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
