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

public class ServiceSpringContextUnitTests extends ContextTests
{
    @Autowired
    LibraryService libraryService;
    
    @MockBean
    LibraryRepository repo;
    
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
    public void getArtistByIdOne()
    {
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(ART_1);
        
        assertThat(libraryService.getArtistById(ART1_ID)).isEqualTo(ART_1);
        
        Mockito.verify(repo, times(1)).readArtistById(ART1_ID);
    }
    
    @Test
    public void getArtistByIdZero()
    {
        assertThat(libraryService.getArtistById(0)).isNull();
        
        Mockito.verify(repo, times(1)).readArtistById(0);
    }
    
    @Test
    public void getArtistByName()
    {
        Mockito.when(repo.readArtistByName(ART1_NAME)).thenReturn(ART_1);
        
        assertThat(libraryService.getArtistByName(ART1_NAME)).isEqualTo(ART_1);
        
        Mockito.verify(repo, times(1)).readArtistByName(ART1_NAME);
    }
    
    @Test
    public void getArtistByNameNonexistent()
    {
        assertThat(libraryService.getArtistByName(NAME_NONEXISTENT)).isNull();
        
        Mockito.verify(repo, times(1)).readArtistByName(NAME_NONEXISTENT);
    }
    
    @Test
    public void getArtistByNameEmptyOrNull()
    {
        Artist resEmpty = libraryService.getArtistByName("");
        Artist resNull = libraryService.getArtistByName(null);
        
        assertThat(resEmpty).isNull();
        assertThat(resNull).isNull();
        
        Mockito.verify(repo, times(1)).readArtistByName("");
        Mockito.verify(repo, times(1)).readArtistByName(null);
    }
    
    @Test
    public void createArtist()
    {
        Mockito.when(repo.insertArtist(ART1_NAME)).thenReturn(1);
        
        assertThat(libraryService.createArtist(new Artist(1, ART1_NAME))).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).insertArtist(ART1_NAME);
    }
    
    @Test
    public void createArtistEmptyOrNull()
    {
        Mockito.when(repo.insertArtist("")).thenReturn(-3);
        Mockito.when(repo.insertArtist(null)).thenReturn(-3);
        
        assertThat(libraryService.createArtist(new Artist(1, ""))).isEqualTo(-2);
        assertThat(libraryService.createArtist(new Artist(1, null))).isEqualTo(-2);
        
        Mockito.verify(repo, never()).insertArtist("");
        Mockito.verify(repo, never()).insertArtist(null);
    }
    
    @Test
    public void createArtistDuplicate()
    {
        Mockito.when(repo.insertArtist(NAME_DUPLICATE)).thenReturn(-1);
        
        assertThat(libraryService.createArtist(new Artist(1, NAME_DUPLICATE))).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).insertArtist(NAME_DUPLICATE);
    }
    
    @Test
    public void updateArtist()
    {
        Mockito.when(repo.updateArtist(ART1_ID, ART_1)).thenReturn(1);
        
        assertThat(libraryService.updateArtist(ART1_ID, ART_1)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).updateArtist(ART1_ID, ART_1);
    }
    
    @Test
    public void updateArtistDuplicate()
    {
        // -1 when duplicate artist name
        Mockito.when(repo.updateArtist(ART1_ID, ARTIST_DUPLICATE)).thenReturn(-1);
        
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_DUPLICATE)).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).updateArtist(ART1_ID, ARTIST_DUPLICATE);
    }
    
    @Test
    public void updateArtistEmptyOrNull()
    {
        // if artist name is empty or null, returns -3
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.updateArtist(ART1_ID, ARTIST_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, never()).updateArtist(ART1_ID, ARTIST_EMPTY);
        Mockito.verify(repo, never()).updateArtist(ART1_ID, ARTIST_NULL);
    }
    
    @Test
    public void updateArtistWrongId()
    {
        // 0 when nothing updated
        Mockito.when(repo.updateArtist(0, ART_1)).thenReturn(0);
        
        assertThat(libraryService.updateArtist(0, ART_1)).isEqualTo(0);
        
        Mockito.verify(repo, times(1)).updateArtist(0, ART_1);
    }
    
    @Test
    public void deleteArtistIdOne()
    {
        Mockito.when(repo.deleteArtist(ART1_ID)).thenReturn(1);
        
        assertThat(libraryService.deleteArtist(ART1_ID)).isEqualTo(1);
    }
    
    @Test
    public void deleteArtistIdZero()
    {
        Mockito.when(repo.deleteArtist(0)).thenReturn(0);
        
        assertThat(libraryService.deleteArtist(0)).isEqualTo(0);
    }
    
    @Test
    public void getTracks()
    {
        Mockito.when(repo.readTracks()).thenReturn(TRACKS);
        
        assertThat(libraryService.getTracks()).isEqualTo(TRACKS);
        
        Mockito.verify(repo, times(1)).readTracks();
    }
    
    @Test
    public void getTrackOneOne()
    {
        Mockito.when(repo.readTrack(TRK1_1_ID, ART1_ID)).thenReturn(TRK1_1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.getTrack(ART1_ID, TRK1_1_ID)).isEqualTo(TRK1_1);
        
        Mockito.verify(repo, times(1)).readTrack(TRK1_1_ID, ART1_ID);
    }
    
    @Test
    public void getTrackBadIds()
    {
        Mockito.when(repo.readTrack(TRK1_1_ID, ART1_ID)).thenReturn(TRK1_1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.getTrack(0, TRK1_1_ID)).isEqualTo(-2);
        assertThat(libraryService.getTrack(ART1_ID, 0)).isNull();
        
        Mockito.verify(repo, times(1)).readTrack(0, ART1_ID);
        Mockito.verify(repo, never()).readTrack(TRK1_1_ID, 0);
    }
    
    @Test
    public void addTrack()
    {
        Mockito.when(repo.insertTrack(ART1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.addTrack(ART1_ID, TRK1_1)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).insertTrack(eq(ART1_ID), any());
    }
    
    @Test
    public void addTrackWrongArtistId()
    {
        Mockito.when(repo.insertTrack(ART1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.addTrack(0, TRK1_1)).isEqualTo(-2);
        
        Mockito.verify(repo, never()).insertTrack(anyInt(), any());
    }
    
    @Test
    public void addTrackDuplicate()
    {
        Mockito.when(repo.insertTrack(ART1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.insertTrack(ART1_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.addTrack(ART1_ID, TRACK_DUPLICATE)).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).insertTrack(eq(ART1_ID), any());
    }
    
    @Test
    public void addTrackEmptyOrNull()
    {
        Mockito.when(repo.insertTrack(ART1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.addTrack(ART1_ID, TRACK_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.addTrack(ART1_ID, TRACK_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, never()).insertTrack(anyInt(), any());
    }
    
    @Test
    public void updateTrack()
    {
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, TRK1_1_ID, TRK1_1);
    }
    
    @Test
    public void updateTrackDuplicate()
    {
        // -1 duplicate
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE)).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, TRK1_1_ID, TRACK_DUPLICATE);
    }
    
    @Test
    public void updateTrackEmptyOrNull()
    {
        // -3 name empty
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_EMPTY)).isEqualTo(-3);
        assertThat(libraryService.updateTrack(ART1_ID, TRK1_1_ID, TRACK_NULL)).isEqualTo(-3);
        
        Mockito.verify(repo, never()).updateTrack(anyInt(), anyInt(), any());
    }
    
    @Test
    public void updateTrackWrongIds()
    {
        // 0 track not found
        // -2 artist not found
        Mockito.when(repo.updateTrack(ART1_ID, TRK1_1_ID, TRK1_1)).thenReturn(1);
        Mockito.when(repo.updateTrack(ART1_ID, 0, TRK1_1)).thenReturn(0);
        Mockito.when(repo.updateTrack(0, TRK1_1_ID, TRK1_1)).thenReturn(-2);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.updateTrack(ART1_ID, 0, TRK1_1)).isEqualTo(0);
        assertThat(libraryService.updateTrack(0, TRK1_1_ID, TRK1_1)).isEqualTo(-2);
        
        Mockito.verify(repo, times(1)).updateTrack(ART1_ID, 0, TRK1_1);
    }
    
    @Test
    public void deleteTrack()
    {
        Mockito.when(repo.deleteTrack(ART1_ID, TRK1_1_ID)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(ART1_ID, TRK1_1_ID)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).deleteTrack(ART1_ID, TRK1_1_ID);
    }
    
    @Test
    public void deleteTrackWrongArtistId()
    {
        Mockito.when(repo.deleteTrack(0, TRK1_1_ID)).thenReturn(0);
        Mockito.when(repo.deleteTrack(ART1_ID, TRK1_1_ID)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(0, TRK1_1_ID)).isEqualTo(-2);
        
        Mockito.verify(repo, never()).deleteTrack(anyInt(), anyInt());
    }
    
    @Test
    public void deleteTrackWrongTrackId()
    {
        Mockito.when(repo.deleteTrack(ART1_ID, 0)).thenReturn(0);
        Mockito.when(repo.deleteTrack(ART1_ID, TRK1_1_ID)).thenReturn(1);
        Mockito.when(repo.readArtistById(ART1_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(ART1_ID, 0)).isEqualTo(0);
        
        Mockito.verify(repo, times(1)).deleteTrack(anyInt(), anyInt());
        Mockito.verify(repo, times(1)).deleteTrack(ART1_ID, 0);
    }
}
