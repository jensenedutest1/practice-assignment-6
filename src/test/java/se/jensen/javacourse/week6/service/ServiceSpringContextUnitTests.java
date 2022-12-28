package se.jensen.javacourse.week6.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;
import se.jensen.javacourse.week6.repository.LibraryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ServiceSpringContextUnitTests extends ContextTests
{
    @Autowired
    LibraryService libraryService;
    
    @MockBean
    LibraryRepository repo;
    
    final static String ARTIST_NAME = "Artist";
    final static String ARTIST_NAME_TWO = "Artist 2";
    final static String ARTIST_NAME_NONEXISTENT = "Nonexistent";
    final static String ARTIST_DUPLICATE_NAME = "Duplicate";
    final static int ARTIST_ID = 1;
    final static int TRACK_ID = 3;
    final static Artist ARTIST_NOMINAL = new Artist(ARTIST_ID, ARTIST_NAME);
    final static Artist ARTIST_DUPLICATE = new Artist(ARTIST_ID, ARTIST_DUPLICATE_NAME);
    final static Artist ARTIST_EMPTY = new Artist(ARTIST_ID, "");
    final static Artist ARTIST_NULL = new Artist(ARTIST_ID, null);
    final static HashMap<Integer, Artist> ARTISTS = new HashMap<>();
    final static ArrayList<String> ARTIST_NAMES = new ArrayList<>();
    final static String TRACK_NAME = "Track";
    final static int TRACK_YEAR = 2000;
    final static Track TRACK_NOMINAL = new Track(TRACK_ID, TRACK_NAME, TRACK_YEAR, ARTIST_ID);
    final static Track TRACK_DUPLICATE = new Track("Duplicate", TRACK_YEAR);
    final static Track TRACK_EMPTY = new Track("", TRACK_YEAR);
    final static Track TRACK_NULL = new Track(null, TRACK_YEAR);
    final static List<Track> TRACKS = new ArrayList<>();
    
    @BeforeAll
    public static void beforeAll()
    {
        ARTIST_NAMES.add(ARTIST_NAME);
        ARTIST_NAMES.add(ARTIST_NAME_TWO);
        TRACKS.add(TRACK_NOMINAL);
        TRACKS.add(new Track(4, "Track 2", 2002, 2));
        ARTIST_NOMINAL.addTrack(TRACK_NOMINAL);
        ARTISTS.put(ARTIST_ID, ARTIST_NOMINAL);
    }
    
    @Test
    public void getArtists()
    {
        Mockito.when(repo.readArtists()).thenReturn(ARTISTS);
        
        HashMap<Integer, Artist> res = libraryService.getArtists();
        
        assertThat(res).isNotNull();
        assertThat(res.get(ARTIST_ID)).isNotNull();
        assertThat(res.get(ARTIST_ID).getName()).isEqualTo(ARTIST_NAME);
        assertThat(res.get(ARTIST_ID).tracks.get(TRACK_ID)).isEqualTo(TRACK_NOMINAL);
        
        Mockito.verify(repo, times(1)).readArtists();
    }
    
    @Test
    public void getArtistsNamesOnly()
    {
        
        Mockito.when(repo.readArtistNames()).thenReturn(ARTIST_NAMES);
        
        List<String> res = libraryService.getArtistsNamesOnly();
        
        assertThat(res).isNotNull();
        assertThat(res).hasSize(2);
        assertThat(res.get(0)).isEqualTo(ARTIST_NAME);
        assertThat(res.get(1)).isEqualTo(ARTIST_NAME_TWO);
        
        Mockito.verify(repo, times(1)).readArtistNames();
    }
    
    @Test
    public void getArtistById()
    {
        Mockito.when(repo.readArtistById(ARTIST_ID)).thenReturn(ARTIST_NOMINAL);
        
        Artist resArtistOne = libraryService.getArtistById(1);
        Artist resArtistNull = libraryService.getArtistById(0);
        
        assertThat(resArtistOne).isNotNull();
        assertThat(resArtistOne.getId()).isEqualTo(ARTIST_ID);
        assertThat(resArtistOne.getName()).isEqualTo(ARTIST_NAME);
        assertThat(resArtistNull).isNull();
        
        Mockito.verify(repo, times(1)).readArtistById(ARTIST_ID);
        Mockito.verify(repo, times(1)).readArtistById(0);
    }
    
    @Test
    public void getArtistByName()
    {
        Mockito.when(repo.readArtistByName(ARTIST_NAME)).thenReturn(ARTIST_NOMINAL);
        Mockito.when(repo.readArtistByName("")).thenReturn(null);
        Mockito.when(repo.readArtistByName(null)).thenReturn(null);
        
        Artist resOne = libraryService.getArtistByName(ARTIST_NAME);
        Artist resNonexistent = libraryService.getArtistByName(ARTIST_NAME_NONEXISTENT);
        Artist resEmpty = libraryService.getArtistByName("");
        Artist resNull = libraryService.getArtistByName(null);
        
        assertThat(resOne).isNotNull();
        assertThat(resOne.getId()).isEqualTo(ARTIST_ID);
        assertThat(resOne.getName()).isEqualTo(ARTIST_NAME);
        assertThat(resEmpty).isNull();
        assertThat(resNonexistent).isNull();
        assertThat(resNull).isNull();
        
        Mockito.verify(repo, times(1)).readArtistByName(ARTIST_NAME);
        Mockito.verify(repo, times(1)).readArtistByName(ARTIST_NAME_NONEXISTENT);
        Mockito.verify(repo, times(1)).readArtistByName("");
        Mockito.verify(repo, times(1)).readArtistByName(null);
    }
    
    @Test
    public void createArtist()
    {
        Mockito.when(repo.insertArtist(ARTIST_NAME)).thenReturn(1);
        Mockito.when(repo.insertArtist("")).thenReturn(-3);
        Mockito.when(repo.insertArtist(null)).thenReturn(-3);
        Mockito.when(repo.insertArtist(ARTIST_DUPLICATE_NAME)).thenReturn(-1);
        
        int resName = libraryService.createArtist(new Artist(1, ARTIST_NAME));
        int resEmpty = libraryService.createArtist(new Artist(1, ""));
        int resNull = libraryService.createArtist(new Artist(1, null));
        int resDuplicate = libraryService.createArtist(new Artist(1, ARTIST_DUPLICATE_NAME));
        
        assertThat(resName).isEqualTo(1);
        assertThat(resEmpty).isEqualTo(-2);
        assertThat(resNull).isEqualTo(-2);
        assertThat(resDuplicate).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).insertArtist(ARTIST_NAME);
        Mockito.verify(repo, times(1)).insertArtist(ARTIST_DUPLICATE_NAME);
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
        Mockito.when(repo.updateArtist(ARTIST_ID, ARTIST_NOMINAL)).thenReturn(1);
        Mockito.when(repo.updateArtist(ARTIST_ID, ARTIST_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.updateArtist(0, ARTIST_NOMINAL)).thenReturn(0);
        
        int resGood = libraryService.updateArtist(ARTIST_ID, ARTIST_NOMINAL);
        int resDuplicate = libraryService.updateArtist(ARTIST_ID, ARTIST_DUPLICATE);
        int resBadId = libraryService.updateArtist(0, ARTIST_NOMINAL);
        int resEmpty = libraryService.updateArtist(ARTIST_ID, ARTIST_EMPTY);
        int resNull = libraryService.updateArtist(ARTIST_ID, ARTIST_NULL);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resBadId).isEqualTo(0);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateArtist(ARTIST_ID, ARTIST_NOMINAL);
        Mockito.verify(repo, times(1)).updateArtist(ARTIST_ID, ARTIST_DUPLICATE);
        Mockito.verify(repo, times(1)).updateArtist(0, ARTIST_NOMINAL);
        Mockito.verify(repo, never()).updateArtist(ARTIST_ID, ARTIST_EMPTY);
        Mockito.verify(repo, never()).updateArtist(ARTIST_ID, ARTIST_NULL);
    }
    
    @Test
    public void deleteArtist()
    {
        Mockito.when(repo.deleteArtist(0)).thenReturn(0);
        Mockito.when(repo.deleteArtist(ARTIST_ID)).thenReturn(1);
        
        assertThat(libraryService.deleteArtist(0)).isEqualTo(0);
        assertThat(libraryService.deleteArtist(ARTIST_ID)).isEqualTo(1);
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
        Mockito.when(repo.readTrack(TRACK_ID, ARTIST_ID)).thenReturn(TRACK_NOMINAL);
        Mockito.when(repo.readArtistById(ARTIST_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.getTrack(0, TRACK_ID)).isEqualTo(-2);
        assertThat(libraryService.getTrack(ARTIST_ID, 0)).isNull();
        assertThat(libraryService.getTrack(ARTIST_ID, TRACK_ID)).isEqualTo(TRACK_NOMINAL);
        
        Mockito.verify(repo, times(1)).readTrack(TRACK_ID, ARTIST_ID);
        Mockito.verify(repo, times(1)).readTrack(0, ARTIST_ID);
        Mockito.verify(repo, never()).readTrack(TRACK_ID, 0);
    }
    
    @Test
    public void addTrack()
    {
        Mockito.when(repo.insertTrack(ARTIST_ID, TRACK_NOMINAL)).thenReturn(1);
        Mockito.when(repo.insertTrack(ARTIST_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.readArtistById(ARTIST_ID)).thenReturn(new Artist());
        
        int resGood = libraryService.addTrack(ARTIST_ID, TRACK_NOMINAL);
        int resDuplicate = libraryService.addTrack(ARTIST_ID, TRACK_DUPLICATE);
        int resNoArtist = libraryService.addTrack(0, TRACK_NOMINAL);
        int resEmpty = libraryService.addTrack(ARTIST_ID, TRACK_EMPTY);
        int resNull = libraryService.addTrack(ARTIST_ID, TRACK_NULL);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resNoArtist).isEqualTo(-2);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).insertTrack(ARTIST_ID, TRACK_NOMINAL);
        Mockito.verify(repo, times(1)).insertTrack(ARTIST_ID, TRACK_DUPLICATE);
        Mockito.verify(repo, never()).insertTrack(0, TRACK_NOMINAL);
        Mockito.verify(repo, never()).insertTrack(ARTIST_ID, TRACK_EMPTY);
        Mockito.verify(repo, never()).insertTrack(ARTIST_ID, TRACK_NULL);
    }
    
    @Test
    public void updateTrack()
    {
        // 1 good
        // 0 track not found
        // -2 artist not found
        // -1 duplicate
        // -3 name empty
        Mockito.when(repo.updateTrack(ARTIST_ID, TRACK_ID, TRACK_NOMINAL)).thenReturn(1);
        Mockito.when(repo.updateTrack(ARTIST_ID, TRACK_ID, TRACK_DUPLICATE)).thenReturn(-1);
        Mockito.when(repo.updateTrack(ARTIST_ID, 0, TRACK_NOMINAL)).thenReturn(0);
        Mockito.when(repo.updateTrack(0, TRACK_ID, TRACK_NOMINAL)).thenReturn(-2);
        Mockito.when(repo.readArtistById(ARTIST_ID)).thenReturn(new Artist());
        
        int resGood = libraryService.updateTrack(ARTIST_ID, TRACK_ID, TRACK_NOMINAL);
        int resDuplicate = libraryService.updateTrack(ARTIST_ID, TRACK_ID, TRACK_DUPLICATE);
        int resNoTrack = libraryService.updateTrack(ARTIST_ID, 0, TRACK_NOMINAL);
        int resNoArtist = libraryService.updateTrack(0, TRACK_ID, TRACK_NOMINAL);
        int resEmpty = libraryService.updateTrack(ARTIST_ID, TRACK_ID, TRACK_EMPTY);
        int resNull = libraryService.updateTrack(ARTIST_ID, TRACK_ID, TRACK_NULL);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resNoTrack).isEqualTo(0);
        assertThat(resNoArtist).isEqualTo(-2);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateTrack(ARTIST_ID, TRACK_ID, TRACK_NOMINAL);
        Mockito.verify(repo, times(1)).updateTrack(ARTIST_ID, TRACK_ID, TRACK_DUPLICATE);
        Mockito.verify(repo, times(1)).updateTrack(ARTIST_ID, 0, TRACK_NOMINAL);
        Mockito.verify(repo, never()).updateTrack(0, TRACK_ID, TRACK_NOMINAL);
        Mockito.verify(repo, never()).updateTrack(ARTIST_ID, TRACK_ID, TRACK_EMPTY);
        Mockito.verify(repo, never()).updateTrack(ARTIST_ID, TRACK_ID, TRACK_NULL);
    }
    
    @Test
    public void deleteTrack()
    {
        Mockito.when(repo.deleteTrack(ARTIST_ID, 0)).thenReturn(0);
        Mockito.when(repo.deleteTrack(ARTIST_ID, TRACK_ID)).thenReturn(1);
        Mockito.when(repo.readArtistById(ARTIST_ID)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(0, 1)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(0, 0)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(ARTIST_ID, 0)).isEqualTo(0);
        assertThat(libraryService.deleteTrack(ARTIST_ID, TRACK_ID)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).deleteTrack(ARTIST_ID, 0);
        Mockito.verify(repo, times(1)).deleteTrack(ARTIST_ID, TRACK_ID);
        Mockito.verify(repo, never()).deleteTrack(0, 1);
        Mockito.verify(repo, never()).deleteTrack(0, 0);
    }
}
