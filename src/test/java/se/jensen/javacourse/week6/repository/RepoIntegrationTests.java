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
    }
    
    @Test
    public void readArtistNames()
    {
    }
    
    @Test
    public void readArtistById()
    {
    }
    
    @Test
    public void readArtistByName()
    {
    }
    
    @Test
    public void insertArtist()
    {
    }
    
    @Test
    public void updateArtist()
    {
    }
    
    @Test
    public void deleteArtist()
    {
    }
    
    @Test
    public void readTracks()
    {
    }
    
    @Test
    public void readTrack()
    {
    }
    
    @Test
    public void insertTrack()
    {
    }
    
    @Test
    public void updateTrack()
    {
    }
    
    @Test
    public void deleteTrack()
    {
    }
}
