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
        Mockito.when(repo.readArtists()).thenReturn(ARTISTS_MAP);
        
        assertThat(libraryService.getArtists()).isEqualTo(ARTISTS_MAP);
        
        Mockito.verify(repo, times(1)).readArtists();
    }
    
    @Test
    public void getArtistsNamesOnly()
    {
        
        Mockito.when(repo.readArtistNames()).thenReturn(ARTIST_NAMES);
        
        assertThat(libraryService.getArtistsNamesOnly()).isEqualTo(ARTIST_NAMES);
        
        Mockito.verify(repo, times(1)).readArtistNames();
    }
    
    @Test
    public void getArtistById()
    {
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(ART_1);
        
        assertThat(libraryService.getArtistById(1)).isEqualTo(ART_1);
        assertThat(libraryService.getArtistById(0)).isNull();
        
        Mockito.verify(repo, times(1)).readArtistById(ART1_ID);
        Mockito.verify(repo, times(1)).readArtistById(0);
    }
    
    @Test
    public void getArtistByName()
    {
        Mockito.when(repo.readArtistByName(ART1_NAME)).thenReturn(ART_1);
        Mockito.when(repo.readArtistByName("")).thenReturn(null);
        Mockito.when(repo.readArtistByName(null)).thenReturn(null);
        
        assertThat(libraryService.getArtistByName(ART1_NAME)).isEqualTo(ART_1);
        assertThat(libraryService.getArtistByName("")).isNull();
        assertThat(libraryService.getArtistByName(NAME_NONEXISTENT)).isNull();
        assertThat(libraryService.getArtistByName(null)).isNull();
        
        Mockito.verify(repo, times(1)).readArtistByName(ART1_NAME);
        Mockito.verify(repo, times(1)).readArtistByName(NAME_NONEXISTENT);
        Mockito.verify(repo, times(1)).readArtistByName("");
        Mockito.verify(repo, times(1)).readArtistByName(null);
    }
    
    @Test
    public void createArtist()
    {
        Mockito.when(repo.insertArtist(ART1_NAME)).thenReturn(1);
        Mockito.when(repo.insertArtist("")).thenReturn(-3);
        Mockito.when(repo.insertArtist(null)).thenReturn(-3);
        Mockito.when(repo.insertArtist(NAME_DUPLICATE)).thenReturn(-1);
        
        assertThat(libraryService.createArtist(new Artist(ART1_ID, ART1_NAME))).isEqualTo(1);
        assertThat(libraryService.createArtist(new Artist(ART1_ID, ""))).isEqualTo(-2);
        assertThat(libraryService.createArtist(new Artist(ART1_ID, null))).isEqualTo(-2);
        assertThat(libraryService.createArtist(new Artist(ART1_ID, NAME_DUPLICATE))).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).insertArtist(ART1_NAME);
        Mockito.verify(repo, times(1)).insertArtist(NAME_DUPLICATE);
        Mockito.verify(repo, never()).insertArtist("");
        Mockito.verify(repo, never()).insertArtist(null);
    }
    
    @Test
    public void updateArtist()
    {
        // if artist name is empty or null, returns -3
        // otherwise returns the result of repo.updateArtist
        // and repo.updateArtist returns
        //  1 when updated,
        //  0 when nothing updated
        //  -1 when duplicate artist name
        //  -3 when exception
        Mockito.when(repo.updateArtist(ART1_ID, ART_1)).thenReturn(1);
        Mockito.when(repo.updateArtist(ART1_ID, ARTIST_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.updateArtist(0, ART_1)).thenReturn(0);
        
        assertThat(libraryService.updateArtist(ART1_ID, ART_1)).isEqualTo(1);
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_DUPLICATE)).isEqualTo(-1);
        assertThat(libraryService.updateArtist(0, ART_1)).isEqualTo(0);
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateArtist(ART1_ID, ART_1);
        Mockito.verify(repo, times(1)).updateArtist(ART1_ID, ARTIST_DUPLICATE);
        Mockito.verify(repo, times(1)).updateArtist(0, ART_1);
        Mockito.verify(repo, never()).updateArtist(ART1_ID, ARTIST_EMPTY);
        Mockito.verify(repo, never()).updateArtist(ART1_ID, ARTIST_NULL);
    }
    
    @Test
    public void deleteArtist()
    {
        Mockito.when(repo.deleteArtist(0)).thenReturn(0);
        Mockito.when(repo.deleteArtist(ART1_ID)).thenReturn(1);
        
        assertThat(libraryService.deleteArtist(0)).isEqualTo(0);
        assertThat(libraryService.deleteArtist(ART1_ID)).isEqualTo(1);
    }
    
    @Test
    public void getTracks()
    {
        Mockito.when(repo.readTracks()).thenReturn(TRACKS);
        
        assertThat(libraryService.getTracks()).isEqualTo(TRACKS);
        
        Mockito.verify(repo, times(1)).readTracks();
    }
    
    @Test
    public void getTrack()
    {
        Mockito.when(repo.readTrack(TRK1_1_ID, ART1_ID)).thenReturn(TRK1_1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.getTrack(0, TRK1_1_ID)).isEqualTo(-2);
        assertThat(libraryService.getTrack(ART1_ID, 0)).isNull();
        assertThat(libraryService.getTrack(ART1_ID, TRK1_1_ID)).isEqualTo(TRK1_1);
        
        Mockito.verify(repo, times(1)).readTrack(TRK1_1_ID, ART1_ID);
        Mockito.verify(repo, times(1)).readTrack(0, ART1_ID);
        Mockito.verify(repo, never()).readTrack(TRK1_1_ID, 0);
    }
    
    @Test
    public void addTrack()
    {
        Mockito.when(repo.insertTrack(ART1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.insertTrack(ART1_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.addTrack(ART1_ID, TRK1_1)).isEqualTo(1);
        assertThat(libraryService.addTrack(ART1_ID, TRACK_DUPLICATE)).isEqualTo(-1);
        assertThat(libraryService.addTrack(0, TRK1_1)).isEqualTo(-2);
        assertThat(libraryService.addTrack(ART1_ID, TRACK_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.addTrack(ART1_ID, TRACK_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).insertTrack(ART1_ID, TRK1_1);
        Mockito.verify(repo, times(1)).insertTrack(ART1_ID, TRACK_DUPLICATE);
        Mockito.verify(repo, never()).insertTrack(0, TRK1_1);
        Mockito.verify(repo, never()).insertTrack(ART1_ID, TRACK_EMPTY);
        Mockito.verify(repo, never()).insertTrack(ART1_ID, TRACK_NULL);
    }
    
    @Test
    public void updateTrack()
    {
        // 1 good
        // 0 track not found
        // -2 artist not found
        // -1 duplicate
        // -3 name empty
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.updateTrack(ART1_ID, 0, TRK1_1)).thenReturn(0);
        Mockito.when(repo.updateTrack(0, TRK1_1_ID, TRK1_1)).thenReturn(-2);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).isEqualTo(1);
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE)).isEqualTo(-1);
        assertThat(libraryService.updateTrack(ART1_ID, 0, TRK1_1)).isEqualTo(0);
        assertThat(libraryService.updateTrack(0, TRK1_1_ID, TRK1_1)).isEqualTo(-2);
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, TRK1_1_ID, TRK1_1);
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE);
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, 0, TRK1_1);
        Mockito.verify(repo, never()).updateTrack(0, TRK1_1_ID, TRK1_1);
        Mockito.verify(repo, never()).updateTrack(ART1_ID, TRK1_1_ID, TRACK_EMPTY);
        Mockito.verify(repo, never()).updateTrack(ART1_ID, TRK1_1_ID, TRACK_NULL);
    }
    
    @Test
    public void deleteTrack()
    {
        Mockito.when(repo.deleteTrack(ART1_ID, 0)).thenReturn(0);
        Mockito.when(repo.deleteTrack(ART1_ID, TRK1_1_ID)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(0, TRK1_1_ID)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(0, 0)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(ART1_ID, 0)).isEqualTo(0);
        assertThat(libraryService.deleteTrack(ART1_ID, TRK1_1_ID)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).deleteTrack(ART1_ID, 0);
        Mockito.verify(repo, times(1)).deleteTrack(ART1_ID, TRK1_1_ID);
        Mockito.verify(repo, never()).deleteTrack(0, 1);
        Mockito.verify(repo, never()).deleteTrack(0, 0);
    }
}
