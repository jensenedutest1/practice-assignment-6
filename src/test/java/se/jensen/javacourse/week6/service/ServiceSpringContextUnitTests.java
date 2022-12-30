package se.jensen.javacourse.week6.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.repository.LibraryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Testing the service layer by mocking the repository layer.
 */
public class ServiceSpringContextUnitTests extends ContextTests
{
    @Autowired
    LibraryService libraryService;
    
    @MockBean
    LibraryRepository repo;
    
    @Test
    public void getArtists()
    {
    }
    
    @Test
    public void getArtistsNamesOnly()
    {
    }
    
    @Test
    public void getArtistByIdOne()
    {
    }
    
    @Test
    public void getArtistByIdZero()
    {
    }
    
    @Test
    public void getArtistByName()
    {
    }
    
    @Test
    public void getArtistByNameNonexistent()
    {
    }
    
    @Test
    public void getArtistByNameEmptyOrNull()
    {
    }
    
    @Test
    public void createArtist()
    {
    }
    
    @Test
    public void createArtistEmptyOrNull()
    {
    }
    
    @Test
    public void createArtistDuplicate()
    {
    }
    
    @Test
    public void updateArtist()
    {
    }
    
    @Test
    public void updateArtistDuplicate()
    {
    }
    
    @Test
    public void updateArtistEmptyOrNull()
    {
    }
    
    @Test
    public void updateArtistWrongId()
    {
    }
    
    @Test
    public void deleteArtistIdOne()
    {
    }
    
    @Test
    public void deleteArtistIdZero()
    {
    }
    
    @Test
    public void getTracks()
    {
    }
    
    @Test
    public void getTrackOneOne()
    {
    }
    
    @Test
    public void getTrackBadIds()
    {
    }
    
    @Test
    public void addTrack()
    {
    }
    
    @Test
    public void addTrackWrongArtistId()
    {
    }
    
    @Test
    public void addTrackDuplicate()
    {
    }
    
    @Test
    public void addTrackEmptyOrNull()
    {
    }
    
    @Test
    public void updateTrack()
    {
    }
    
    @Test
    public void updateTrackDuplicate()
    {
    }
    
    @Test
    public void updateTrackEmptyOrNull()
    {
    }
    
    @Test
    public void updateTrackWrongIds()
    {
    }
    
    @Test
    public void deleteTrack()
    {
    }
    
    @Test
    public void deleteTrackWrongArtistId()
    {
    }
    
    @Test
    public void deleteTrackWrongTrackId()
    {
    }
}
