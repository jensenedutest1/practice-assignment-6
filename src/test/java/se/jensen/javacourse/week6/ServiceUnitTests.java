package se.jensen.javacourse.week6;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.database.LibraryRepository;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;
import se.jensen.javacourse.week6.service.LibraryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
public class ServiceUnitTests
{
    @Autowired
    LibraryService libraryService;
    
    @MockBean
    LibraryRepository repo;
    
    @Test
    public void getArtists()
    {
        final String artistName = "Artist";
        final int artistId = 1;
        HashMap<Integer, Artist> artists = new HashMap<>();
        artists.put(artistId, new Artist(artistId, artistName));
        
        Mockito.when(repo.readArtists()).thenReturn(artists);
        
        HashMap<Integer, Artist> res = libraryService.getArtists();
        
        assertThat(res).isNotNull();
        assertThat(res.get(artistId)).isNotNull();
        assertThat(res.get(artistId).getName()).isEqualTo(artistName);
        
        Mockito.verify(repo, times(1)).readArtists();
    }
    
    @Test
    public void getArtistsNamesOnly()
    {
        final String artistName = "Artist";
        final String artistNameTwo = "Artist 2";
        ArrayList<String> artists = new ArrayList<>();
        artists.add(artistName);
        artists.add(artistNameTwo);
        
        Mockito.when(repo.readArtistNames()).thenReturn(artists);
        
        List<String> res = libraryService.getArtistsNamesOnly();
        
        assertThat(res).isNotNull();
        assertThat(res).hasSize(2);
        assertThat(res.get(0)).isEqualTo(artistName);
        assertThat(res.get(1)).isEqualTo(artistNameTwo);
        
        Mockito.verify(repo, times(1)).readArtistNames();
    }
    
    @Test
    public void getArtistById()
    {
        final String artistName = "Artist";
        final int artistId = 1;
        final Artist artist = new Artist(artistId, artistName);
        
        Mockito.when(repo.readArtistById(1)).thenReturn(artist);
        Mockito.when(repo.readArtistById(0)).thenReturn(null);
        
        Artist artistOne = libraryService.getArtistById(1);
        Artist artistNull = libraryService.getArtistById(0);
        
        assertThat(artistOne).isNotNull();
        assertThat(artistOne.getId()).isEqualTo(artistId);
        assertThat(artistOne.getName()).isEqualTo(artistName);
        assertThat(artistNull).isNull();
        
        Mockito.verify(repo, times(1)).readArtistById(1);
        Mockito.verify(repo, times(1)).readArtistById(0);
    }
    
    @Test
    public void getArtistByName()
    {
        final String artistName = "Artist";
        final String artistNameNonexistent = "Nonexistent";
        final int artistId = 1;
        final Artist artist = new Artist(artistId, artistName);
        
        Mockito.when(repo.readArtistByName(artistName)).thenReturn(artist);
        Mockito.when(repo.readArtistByName("")).thenReturn(null);
        Mockito.when(repo.readArtistByName(null)).thenReturn(null);
        
        Artist artistOne = libraryService.getArtistByName(artistName);
        Artist artistNonexistent = libraryService.getArtistByName(artistNameNonexistent);
        Artist artistEmpty = libraryService.getArtistByName("");
        Artist artistNull = libraryService.getArtistByName(null);
        
        assertThat(artistOne).isNotNull();
        assertThat(artistOne.getId()).isEqualTo(artistId);
        assertThat(artistOne.getName()).isEqualTo(artistName);
        assertThat(artistEmpty).isNull();
        assertThat(artistNonexistent).isNull();
        assertThat(artistNull).isNull();
        
        Mockito.verify(repo, times(1)).readArtistByName(artistName);
        Mockito.verify(repo, times(1)).readArtistByName(artistNameNonexistent);
        Mockito.verify(repo, times(1)).readArtistByName("");
        Mockito.verify(repo, times(1)).readArtistByName(null);
    }
    
    @Test
    public void createArtist()
    {
        final String artistName = "Artist";
        final String duplicateName = "Duplicate";
        
        Mockito.when(repo.insertArtist(artistName)).thenReturn(1);
        Mockito.when(repo.insertArtist("")).thenReturn(-3);
        Mockito.when(repo.insertArtist(null)).thenReturn(-3);
        Mockito.when(repo.insertArtist(duplicateName)).thenReturn(-1);
        
        int resName = libraryService.createArtist(new Artist(1, artistName));
        int resEmpty = libraryService.createArtist(new Artist(1, ""));
        int resNull = libraryService.createArtist(new Artist(1, null));
        int resDuplicate = libraryService.createArtist(new Artist(1, duplicateName));
        
        assertThat(resName).isEqualTo(1);
        assertThat(resEmpty).isEqualTo(-2);
        assertThat(resNull).isEqualTo(-2);
        assertThat(resDuplicate).isEqualTo(-1);
        
        Mockito.verify(repo, times(1)).insertArtist(artistName);
        Mockito.verify(repo, times(1)).insertArtist(duplicateName);
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
        final String artistName = "Artist";
        final String artistDuplicateName = "Duplicate";
        
        Artist artistGood = new Artist(1, artistName);
        Artist artistDuplicate = new Artist(1, artistDuplicateName);
        Artist artistEmpty = new Artist(1, "");
        Artist artistNull = new Artist(1, null);
        
        Mockito.when(repo.updateArtist(1, artistGood)).thenReturn(1);
        Mockito.when(repo.updateArtist(1, artistDuplicate)).thenReturn(-1);
        Mockito.when(repo.updateArtist(0, artistGood)).thenReturn(0);
        
        int resGood = libraryService.updateArtist(1, artistGood);
        int resDuplicate = libraryService.updateArtist(1, artistDuplicate);
        int resBadId = libraryService.updateArtist(0, artistGood);
        int resEmpty = libraryService.updateArtist(1, artistEmpty);
        int resNull = libraryService.updateArtist(1, artistNull);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resBadId).isEqualTo(0);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateArtist(1, artistGood);
        Mockito.verify(repo, times(1)).updateArtist(1, artistDuplicate);
        Mockito.verify(repo, times(1)).updateArtist(0, artistGood);
        Mockito.verify(repo, never()).updateArtist(1, artistEmpty);
        Mockito.verify(repo, never()).updateArtist(1, artistNull);
    }
    
    @Test
    public void deleteArtist()
    {
        Mockito.when(repo.deleteArtist(0)).thenReturn(0);
        Mockito.when(repo.deleteArtist(1)).thenReturn(1);
        
        assertThat(libraryService.deleteArtist(0)).isEqualTo(0);
        assertThat(libraryService.deleteArtist(1)).isEqualTo(1);
    }
    
    @Test
    public void getTracks()
    {
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track(3, "Track 1", 2001, 1));
        tracks.add(new Track(4, "Track 2", 2002, 2));
        Mockito.when(repo.readTracks()).thenReturn(tracks);
        
        assertThat(libraryService.getTracks()).isEqualTo(tracks);
        
        Mockito.verify(repo, times(1)).readTracks();
    }
    
    @Test
    public void getTrack()
    {
        Track track = new Track(2, "Track", 2000, 1);
        Mockito.when(repo.readTrack(2, 1)).thenReturn(track);
        Mockito.when(repo.readArtistById(1)).thenReturn(new Artist());
        
        assertThat(libraryService.getTrack(0, 1)).isEqualTo(-2);
        assertThat(libraryService.getTrack(1, 0)).isNull();
        assertThat(libraryService.getTrack(1, 2)).isEqualTo(track);
        
        Mockito.verify(repo, times(1)).readTrack(2, 1);
        Mockito.verify(repo, times(1)).readTrack(0, 1);
        Mockito.verify(repo, never()).readTrack(1, 0);
    }
    
    @Test
    public void addTrack()
    {
        final String trackName = "Track";
        final String trackDuplicateName = "Duplicate";
        final int year = 2000;
        
        Track trackGood = new Track(trackName, year);
        Track trackDuplicate = new Track(trackDuplicateName, year);
        Track trackEmpty = new Track("", year);
        Track trackNull = new Track(null, year);
        
        Mockito.when(repo.insertTrack(1, trackGood)).thenReturn(1);
        Mockito.when(repo.insertTrack(1, trackDuplicate)).thenReturn(-1);
        Mockito.when(repo.readArtistById(1)).thenReturn(new Artist());
        
        int resGood = libraryService.addTrack(1, trackGood);
        int resDuplicate = libraryService.addTrack(1, trackDuplicate);
        int resNoArtist = libraryService.addTrack(0, trackGood);
        int resEmpty = libraryService.addTrack(1, trackEmpty);
        int resNull = libraryService.addTrack(1, trackNull);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resNoArtist).isEqualTo(-2);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).insertTrack(1, trackGood);
        Mockito.verify(repo, times(1)).insertTrack(1, trackDuplicate);
        Mockito.verify(repo, never()).insertTrack(0, trackGood);
        Mockito.verify(repo, never()).insertTrack(1, trackEmpty);
        Mockito.verify(repo, never()).insertTrack(1, trackNull);
    }
    
    @Test
    public void updateTrack()
    {
        // 1 good
        // 0 track not found
        // -2 artist not found
        // -1 duplicate
        // -3 name empty
        final String trackName = "Track";
        final String trackDuplicateName = "Duplicate";
        final int year = 2000;
        
        Track trackGood = new Track(trackName, year);
        Track trackDuplicate = new Track(trackDuplicateName, year);
        Track trackEmpty = new Track("", year);
        Track trackNull = new Track(null, year);
        
        Mockito.when(repo.updateTrack(1, 1, trackGood)).thenReturn(1);
        Mockito.when(repo.updateTrack(1, 1, trackDuplicate)).thenReturn(-1);
        Mockito.when(repo.updateTrack(1, 0, trackGood)).thenReturn(0);
        Mockito.when(repo.updateTrack(0, 1, trackGood)).thenReturn(-2);
        Mockito.when(repo.readArtistById(1)).thenReturn(new Artist());
        
        int resGood = libraryService.updateTrack(1, 1, trackGood);
        int resDuplicate = libraryService.updateTrack(1, 1, trackDuplicate);
        int resNoTrack = libraryService.updateTrack(1, 0, trackGood);
        int resNoArtist = libraryService.updateTrack(0, 1, trackGood);
        int resEmpty = libraryService.updateTrack(1, 1, trackEmpty);
        int resNull = libraryService.updateTrack(1, 1, trackNull);
        
        assertThat(resGood).isEqualTo(1);
        assertThat(resDuplicate).isEqualTo(-1);
        assertThat(resNoTrack).isEqualTo(0);
        assertThat(resNoArtist).isEqualTo(-2);
        assertThat(resEmpty).isEqualTo(-3);
        assertThat(resNull).isEqualTo(-3);
        
        Mockito.verify(repo, times(1)).updateTrack(1, 1, trackGood);
        Mockito.verify(repo, times(1)).updateTrack(1, 1, trackDuplicate);
        Mockito.verify(repo, times(1)).updateTrack(1, 0, trackGood);
        Mockito.verify(repo, never()).updateTrack(0, 1, trackGood);
        Mockito.verify(repo, never()).updateTrack(1, 1, trackEmpty);
        Mockito.verify(repo, never()).updateTrack(1, 1, trackNull);
    }
    
    @Test
    public void deleteTrack()
    {
        Mockito.when(repo.deleteTrack(1, 0)).thenReturn(0);
        Mockito.when(repo.deleteTrack(1, 1)).thenReturn(1);
        Mockito.when(repo.readArtistById(1)).thenReturn(new Artist());
        
        assertThat(libraryService.deleteTrack(0, 1)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(0, 0)).isEqualTo(-2);
        assertThat(libraryService.deleteTrack(1, 0)).isEqualTo(0);
        assertThat(libraryService.deleteTrack(1, 1)).isEqualTo(1);
        
        Mockito.verify(repo, times(1)).deleteTrack(1, 0);
        Mockito.verify(repo, times(1)).deleteTrack(1, 1);
        Mockito.verify(repo, never()).deleteTrack(0, 1);
        Mockito.verify(repo, never()).deleteTrack(0, 0);
    }
}
