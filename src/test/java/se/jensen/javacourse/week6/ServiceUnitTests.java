package se.jensen.javacourse.week6;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;

import se.jensen.javacourse.week6.database.LibraryRepository;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.service.LibraryService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ServiceUnitTests
{
    @Autowired
    LibraryService libraryService;
    
    @MockBean
    LibraryRepository repo;
    
    @Test
    public void getArtistsReturnsArtists()
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
    }
}
