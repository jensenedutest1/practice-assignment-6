package se.jensen.javacourse.week6.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

import se.jensen.javacourse.week6.Tests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;
import se.jensen.javacourse.week6.service.LibraryService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibraryController.class)
class ControllerUnitTests extends Tests
{
    @Autowired
    MockMvc mockMvc;
    @MockBean
    LibraryService service;
    
    final String baseUrl = "/api/v1/";
    
    @Test
    void getArtists() throws Exception
    {
        Mockito.when(service.getArtists()).thenReturn(ARTISTS_MAP);
        
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "artists"))
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(ARTISTS_MAP)));
        
        Mockito.verify(service, times(1)).getArtists();
    }
    
    @Test
    void getArtistIdOne() throws Exception
    {
        Mockito.when(service.getArtistById(ART1_ID)).thenReturn(ART_1);
        
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "artists/" + ART1_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(ART_1)));
        
        Mockito.verify(service, times(1)).getArtistById(ART1_ID);
    }
    
    @Test
    void getArtistIdZero() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "artists/0"))
                .andExpect(status().isOk()).andExpect(content().string(""));
        
        Mockito.verify(service, times(1)).getArtistById(0);
    }
    
    @Test
    void getArtistIdString() throws Exception
    {
        final String artistId = "x";
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "artists/" + artistId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage()
                                                        .contains("Failed to convert value of type")));
        
        Mockito.verify(service, never()).getArtistById(anyInt());
    }
    
    @Test
    void postArtistNewName() throws Exception
    {
        Mockito.when(service.createArtist(ART_1)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(ART_1)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Artist with name " + ART_1.getName() + " created"));
        
        Mockito.verify(service, times(1)).createArtist(ART_1);
    }
    
    @Test
    void postArtistEmptyName() throws Exception
    {
        final Artist artistEmptyName = new Artist(1, "");
        Mockito.when(service.createArtist(artistEmptyName)).thenReturn(-2);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(artistEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot create artist with empty name"));
        
        Mockito.verify(service, times(1)).createArtist(any());
    }
    
    @Test
    void postArtistNullName() throws Exception
    {
        final Artist artistNullName = new Artist(1, null);
        Mockito.when(service.createArtist(artistNullName)).thenReturn(-2);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(artistNullName)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot create artist with empty name"));
        
        Mockito.verify(service, times(1)).createArtist(any());
    }
    
    @Test
    void postArtistDuplicateName() throws Exception
    {
        final Artist artistNameDuplicate = new Artist(1, NAME_DUPLICATE);
        Mockito.when(service.createArtist(artistNameDuplicate)).thenReturn(-1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(artistNameDuplicate)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Artist with that name already exists"));
        
        Mockito.verify(service, times(1)).createArtist(any());
    }
    
    @Test
    void putArtistNewName() throws Exception
    {
        final Artist artistNewName = new Artist(ART1_ID, NEW_ARTIST_NAME);
        Mockito.when(service.updateArtist(ART1_ID, artistNewName)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .put(baseUrl + "artists/" + ART1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(artistNewName)))
                .andExpect(status().isOk());
        
        Mockito.verify(service, times(1)).updateArtist(ART1_ID, artistNewName);
    }
    
    @Test
    void putArtistEmptyName() throws Exception
    {
        final Artist artistEmptyName = new Artist(ART1_ID, "");
        Mockito.when(service.updateArtist(anyInt(), eq(artistEmptyName))).thenReturn(-3);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .put(baseUrl + "artists/" + ART1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(artistEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot set empty name to artist"));
        
        Mockito.verify(service, never()).createArtist(any());
    }
    
    @Test
    void deleteArtist() throws Exception
    {
        Mockito.when(service.deleteArtist(ART1_ID)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "artists/" + ART1_ID))
                .andExpect(status().isOk());
        
        Mockito.verify(service, times(1)).deleteArtist(ART1_ID);
    }
    
    @Test
    void getTracks() throws Exception
    {
        Mockito.when(service.getTracks()).thenReturn(TRACKS);
        
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "tracks")).andExpect(status().isOk());
        
        Mockito.verify(service, times(1)).getTracks();
    }
    
    @Test
    void getTrack() throws Exception
    {
        Mockito.when(service.getTrack(ART1_ID, TRK1_1_ID)).thenReturn(TRK1_1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .get(baseUrl + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(OM.writeValueAsString(TRK1_1)));
        
        Mockito.verify(service, times(1)).getTrack(ART1_ID, TRK1_1_ID);
    }
    
    @Test
    void postTrackNewName() throws Exception
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, NEW_TRACK_NAME, 2022, ART1_ID);
        
        Mockito.when(service.addTrack(ART1_ID, newTrack)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists/" + ART1_ID + "/tracks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Track with name " + newTrack.getName() + " created"));
        
        Mockito.verify(service, times(1)).addTrack(ART1_ID, newTrack);
    }
    
    @Test
    void postTrackDuplicateName() throws Exception
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, TRK1_1_NAME, 2022, ART1_ID);
        
        Mockito.when(service.addTrack(ART1_ID, newTrack)).thenReturn(-1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists/" + ART1_ID + "/tracks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Track with that name already exists"));
        
        Mockito.verify(service, times(1)).addTrack(ART1_ID, newTrack);
    }
    
    @Test
    void postTrackWrongArtistId() throws Exception
    {
        final int artistId = 10;
        final Track newTrack = new Track(TRK3_2_ID + 1, TRK1_1_NAME, 2022, ART1_ID);
        
        Mockito.when(service.addTrack(artistId, newTrack)).thenReturn(-2);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .post(baseUrl + "artists/" + artistId + "/tracks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No artist with that id exists"));
        
        Mockito.verify(service, times(1)).addTrack(artistId, newTrack);
    }
    
    @Test
    void putTrackNewName() throws Exception
    {
        final Track newTrack = new Track(TRK1_1_ID, NEW_TRACK_NAME, 2022, ART1_ID);
        
        Mockito.when(service.updateTrack(ART1_ID, TRK1_1_ID, newTrack)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .put(baseUrl + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isOk());
        
        Mockito.verify(service, times(1)).updateTrack(ART1_ID, TRK1_1_ID, newTrack);
    }
    
    @Test
    void putTrackDuplicateName() throws Exception
    {
        final Track newTrack = new Track(TRK1_2_NAME, 2022);
        
        Mockito.when(service.updateTrack(ART1_ID, TRK1_1_ID, newTrack)).thenReturn(-1);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .put(baseUrl + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Track with that name already exists"));
        
        Mockito.verify(service, times(1)).updateTrack(ART1_ID, TRK1_1_ID, newTrack);
    }
    
    @Test
    void putTrackEmptyName() throws Exception
    {
        final Track newTrack = new Track("", 2022);
        
        Mockito.when(service.updateTrack(ART1_ID, TRK1_1_ID, newTrack)).thenReturn(-3);
        
        mockMvc.perform(MockMvcRequestBuilders
                                .put(baseUrl + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(OM.writeValueAsString(newTrack)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot set empty name to track"));
        
        Mockito.verify(service, times(1)).updateTrack(ART1_ID, TRK1_1_ID, newTrack);
    }
    
    @Test
    void deleteTrackOneOne() throws Exception
    {
        Mockito.when(service.deleteTrack(ART1_ID, TRK1_1_ID)).thenReturn(1);
        
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID))
                .andExpect(status().isOk());
        
        Mockito.verify(service, times(1)).deleteTrack(ART1_ID, TRK1_1_ID);
    }
    
    @Test
    void deleteTrackWrongIdZero() throws Exception
    {
        Mockito.when(service.deleteTrack(ART1_ID, 0)).thenReturn(0);
        
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "artists/" + ART1_ID + "/tracks/" + 0))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No track with that id exists"));
        
        Mockito.verify(service, times(1)).deleteTrack(ART1_ID, 0);
    }
    
    @Test
    void deleteTrackWrongIdString() throws Exception
    {
        final String trackId = "x";
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "artists/" + ART1_ID + "/tracks/" + trackId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage()
                                                        .contains("Failed to convert value of type")));
        
        Mockito.verify(service, never()).deleteTrack(anyInt(), anyInt());
    }
}