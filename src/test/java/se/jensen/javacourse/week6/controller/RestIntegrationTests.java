package se.jensen.javacourse.week6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;

import java.net.URI;
import java.util.List;

import se.jensen.javacourse.week6.ContextTests;
import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end tests: making http requests to the various endpoints with real data.
 * Nothing is mocked, meaning all layers are in play, including an in-memory H2 database.
 */
public class RestIntegrationTests extends ContextTests
{
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String getBaseUrl()
    {
        return "http://localhost:" + port + "/api/v1/";
    }
    
    @Test
    public void getArtists() throws Exception
    {
    }
    
    @Test
    public void getArtistZero()
    {
    }
    
    @Test
    public void getArtistTwo() throws JsonProcessingException
    {
    }
    
    @Test
    public void postArtistNewName()
    {
    }
    
    @Test
    public void postArtistDuplicateName()
    {
    }
    
    @Test
    public void postArtistEmptyName()
    {
    }
    
    @Test
    public void postArtistNullName()
    {
    }
    
    @Test
    public void putArtistNewName()
    {
    }
    
    @Test
    public void putArtistBadIdZero()
    {
    }
    
    @Test
    public void putArtistBadIdString()
    {
    }
    
    @Test
    public void putArtistDuplicateName()
    {
    }
    
    @Test
    public void putArtistSameName()
    {
    }
    
    @Test
    public void putArtistEmptyName()
    {
    }
    
    @Test
    public void putArtistNullName()
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
    public void deleteArtistIdString()
    {
    }
    
    @Test
    public void getTracks() throws Exception
    {
    }
    
    @Test
    public void getTrackOneOne() throws Exception
    {
    }
    
    @Test
    public void postTrackNewName()
    {
    }
    
    @Test
    public void postTrackDuplicateName()
    {
    }
    
    @Test
    public void postTrackEmptyName()
    {
    }
    
    @Test
    public void postTrackNullName()
    {
    }
    
    @Test
    public void postTrackWrongArtistId()
    {
    }
    
    @Test
    public void putTrackNewName()
    {
    }
    
    @Test
    public void putTrackSameName()
    {
    }
    
    @Test
    public void putTrackBadTrackIdZero()
    {
    }
    
    @Test
    public void putTrackBadTrackIdString()
    {
    }
    
    @Test
    public void putTrackDuplicateName()
    {
    }
    
    @Test
    public void putTrackBadArtistIdZero()
    {
    }
    
    @Test
    public void putTrackBadArtistIdString()
    {
    }
    
    @Test
    public void putTrackEmptyName()
    {
    }
    
    @Test
    public void putTrackNullName()
    {
    }
    
    @Test
    public void deleteTrackIdOne()
    {
    }
    
    @Test
    public void deleteTrackWrongIdZero()
    {
    }
    
    @Test
    public void deleteTrackWrongIdString()
    {
    }
    
    @Test
    public void deleteTrackWrongArtistIdZero()
    {
    }
    
    @Test
    public void deleteTrackWrongArtistIdString()
    {
    }
}
