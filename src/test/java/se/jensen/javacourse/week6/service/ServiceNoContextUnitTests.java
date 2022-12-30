package se.jensen.javacourse.week6.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import se.jensen.javacourse.week6.Tests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;
import se.jensen.javacourse.week6.repository.LibraryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * There is no Spring context used in this class. Everything is completely mocked,
 * which means only the basic functionality of the separate function is tested
 * in complete isolation.
 */
public class ServiceNoContextUnitTests extends Tests
{
    static LibraryService libraryService;
    static LibraryRepository repo;
    static Field dbField;
    
    @BeforeAll
    public static void beforeAll() throws NoSuchFieldException, IllegalAccessException
    {
        Tests.beforeAll();
        libraryService = new LibraryService();
        dbField = LibraryService.class.getDeclaredField("db");
        dbField.setAccessible(true);
    }
    
    @BeforeEach
    public void beforeEach() throws IllegalAccessException
    {
        repo = Mockito.mock(LibraryRepository.class);
        dbField.set(libraryService, repo);
    }
    
    @Test
    public void getArtists()
    {
    }
    
    @Test
    public void getArtistsNamesOnly()
    {
    }
    
    @Test
    public void getArtistById()
    {
    }
    
    @Test
    public void getArtistByName()
    {
    }
    
    @Test
    public void createArtist()
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
    public void getTracks()
    {
    }
    
    @Test
    public void getTrack()
    {
    }
    
    @Test
    public void addTrack()
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
