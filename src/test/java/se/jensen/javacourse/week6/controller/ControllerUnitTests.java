package se.jensen.javacourse.week6.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.service.LibraryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
//@AutoConfigureMockMvc
class ControllerUnitTests// extends ContextTests
{
    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    LibraryService service;
    
    final static int ARTIST_ID = 1;
    final static String ARTIST_NAME = "Artist";
    final static Artist ARTIST_NOMINAL = new Artist(ARTIST_ID, ARTIST_NAME);
    
    @Test
    void getArtists()
    {
    }
    
    @Test
    void getArtist() throws Exception
    {
        Mockito.when(service.getArtistById(ARTIST_ID)).thenReturn(ARTIST_NOMINAL);
        
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artists/1"));
        ra.andDo(print()).andExpect(status().isOk());
//        assertThat(controller.getArtist(ARTIST_ID)).isEqualTo(ARTIST_NOMINAL);
//        assertThat(controller.getArtist(0)).isNull();
        
//        Mockito.verify(service, times(1)).getArtistById(ARTIST_ID);
//        Mockito.verify(service, times(1)).getArtistById(0);
    }
    
    @Test
    void postArtist()
    {
    }
    
    @Test
    void putArtist()
    {
    }
    
    @Test
    void deleteArtist()
    {
    }
    
    @Test
    void getTracks()
    {
    }
    
    @Test
    void testGetTracks()
    {
    }
    
    @Test
    void postTrack()
    {
    }
    
    @Test
    void putTrack()
    {
    }
    
    @Test
    void deleteTrack()
    {
    }
}