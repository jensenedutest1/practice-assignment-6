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
        ResponseEntity<String> res = restTemplate.getForEntity(getBaseUrl() + "artists", String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(OM.writeValueAsString(ARTISTS_MAP));
    }
    
    @Test
    public void getArtistZero()
    {
        ResponseEntity<String> res = restTemplate.getForEntity(getBaseUrl() + "artists/0", String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
    }
    
    @Test
    public void getArtistTwo() throws JsonProcessingException
    {
        ResponseEntity<String> res = restTemplate.getForEntity(getBaseUrl() + "artists/" + ART2_ID, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(OM.writeValueAsString(ARTIST_2_NO_TRACKS));
    }
    
    @Test
    public void postArtistNewName()
    {
        final Artist newArtist = new Artist(4, NEW_ARTIST_NAME);
        ResponseEntity<String> res = restTemplate.postForEntity(getBaseUrl() + "artists", newArtist, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isEqualTo("Artist with name " + NEW_ARTIST_NAME + " created");
        
        RowMapper<Artist> rm = (rs, rowNum) -> new Artist(
                rs.getInt("id"),
                rs.getString("name"));
        List<Artist> artists = jdbcTemplate.query("SELECT * FROM artists", rm);
        
        assertThat(artists).contains(newArtist);
        assertThat(artists).hasSize(ARTISTS_MAP.size() + 1);
    }
    
    @Test
    public void postArtistDuplicateName()
    {
        ResponseEntity<String> res = restTemplate.postForEntity(getBaseUrl() + "artists", ART_1, String.class);
        
        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("Artist with that name already exists");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size());
    }
    
    @Test
    public void postArtistEmptyName()
    {
        final Artist newArtist = new Artist(4, "");
        ResponseEntity<String> res = restTemplate.postForEntity(getBaseUrl() + "artists", newArtist, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot create artist with empty name");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size());
    }
    
    @Test
    public void postArtistNullName()
    {
        final Artist newArtist = new Artist(4, "");
        ResponseEntity<String> res = restTemplate.postForEntity(getBaseUrl() + "artists", newArtist, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot create artist with empty name");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size());
    }
    
    @Test
    public void putArtistNewName()
    {
        final Artist newArtist = new Artist(ART1_ID, NEW_ARTIST_NAME);
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).contains(NEW_ARTIST_NAME);
    }
    
    @Test
    public void putArtistBadIdZero()
    {
        final Artist newArtist = new Artist(ART1_ID, NEW_ARTIST_NAME);
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/0"));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No artist with that id exists");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).doesNotContain(NEW_ARTIST_NAME);
    }
    
    @Test
    public void putArtistBadIdString()
    {
        final String id = "x";
        final Artist newArtist = new Artist(ART1_ID, NEW_ARTIST_NAME);
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + id));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + id + "\"");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).doesNotContain(NEW_ARTIST_NAME);
    }
    
    @Test
    public void putArtistDuplicateName()
    {
        final Artist newArtist = new Artist(ART1_ID, ART2_NAME);
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("Another artist with that name already exists");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).containsOnlyOnce(ART2_NAME);
    }
    
    @Test
    public void putArtistSameName()
    {
        final Artist newArtist = new Artist(ART1_ID, ART1_NAME);
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).containsOnlyOnce(ART1_NAME);
    }
    
    @Test
    public void putArtistEmptyName()
    {
        final Artist newArtist = new Artist(4, "");
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot set empty name to artist");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).doesNotContain("");
    }
    
    @Test
    public void putArtistNullName()
    {
        final Artist newArtist = new Artist(4, "");
        RequestEntity<Artist> req = new RequestEntity<>(newArtist, HttpMethod.PUT, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot set empty name to artist");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).doesNotContain((String) null);
    }
    
    @Test
    public void deleteArtistIdOne()
    {
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE, URI.create(getBaseUrl() + "artists/" + ART1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size() - 1);
        assertThat(artistNames).doesNotContain(ART1_NAME);
    }
    
    @Test
    public void deleteArtistIdZero()
    {
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE, URI.create(getBaseUrl() + "artists/0"));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size());
    }
    
    @Test
    public void deleteArtistIdString()
    {
        final String id = "x";
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE, URI.create(getBaseUrl() + "artists/" + id));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + id + "\"");
        
        List<String> artistNames = jdbcTemplate.query("SELECT name FROM artists", (s, n) -> s.getString("name"));
        
        assertThat(artistNames).hasSize(ARTISTS_MAP.size());
    }
    
    @Test
    public void getTracks() throws Exception
    {
        ResponseEntity<String> res = restTemplate.getForEntity(getBaseUrl() + "tracks", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(OM.writeValueAsString(TRACKS));
    }
    
    @Test
    public void getTrackOneOne() throws Exception
    {
        ResponseEntity<String> res = restTemplate.getForEntity(
                getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID, String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isEqualTo(OM.writeValueAsString(TRK1_1));
    }
    
    @Test
    public void postTrackNewName()
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, NEW_TRACK_NAME, 2022, ART1_ID);
        ResponseEntity<String> res = restTemplate.postForEntity(
                getBaseUrl() + "artists/" + ART1_ID + "/tracks", newTrack, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody()).isEqualTo("Track with name " + NEW_TRACK_NAME + " created");
        
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        List<Track> tracks = jdbcTemplate.query("SELECT * FROM tracks", rm);
        
        assertThat(tracks).contains(newTrack);
        assertThat(tracks).hasSize(TRACKS.size() + 1);
    }
    
    @Test
    public void postTrackDuplicateName()
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, TRK1_1_NAME, 2022, ART1_ID);
        ResponseEntity<String> res = restTemplate.postForEntity(
                getBaseUrl() + "artists/" + ART1_ID + "/tracks", newTrack, String.class);
        
        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("Track with that name already exists");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> tracks = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(tracks).containsOnlyOnce(newTrack.getName());
        assertThat(tracks).hasSize(TRACKS.size());
    }
    
    @Test
    public void postTrackEmptyName()
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, "", 2022, ART1_ID);
        ResponseEntity<String> res = restTemplate.postForEntity(
                getBaseUrl() + "artists/" + ART1_ID + "/tracks", newTrack, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot create track with empty name");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> tracks = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(tracks).doesNotContain("");
        assertThat(tracks).hasSize(TRACKS.size());
    }
    
    @Test
    public void postTrackNullName()
    {
        final Track newTrack = new Track(TRK3_2_ID + 1, "", 2022, ART1_ID);
        ResponseEntity<String> res = restTemplate.postForEntity(
                getBaseUrl() + "artists/" + ART1_ID + "/tracks", newTrack, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot create track with empty name");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> tracks = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(tracks).doesNotContain((String) null);
        assertThat(tracks).hasSize(TRACKS.size());
    }
    
    @Test
    public void postTrackWrongArtistId()
    {
        final int wrongArtistId = 10;
        final Track newTrack = new Track(TRK3_2_ID + 1, NEW_TRACK_NAME, 2022, wrongArtistId);
        ResponseEntity<String> res = restTemplate.postForEntity(
                getBaseUrl() + "artists/" + wrongArtistId + "/tracks", newTrack, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No artist with that id exists");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> tracks = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(tracks).doesNotContain(NEW_TRACK_NAME);
        assertThat(tracks).hasSize(TRACKS.size());
    }
    
    @Test
    public void putTrackNewName()
    {
        final Track newTrack = new Track(TRK1_1_ID, NEW_TRACK_NAME, 2022, ART1_ID);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        List<Track> tracks = jdbcTemplate.query("SELECT * FROM tracks", rm);
        
        assertThat(tracks).contains(newTrack);
    }
    
    @Test
    public void putTrackSameName()
    {
        final Track newTrack = new Track(TRK1_1_ID, TRK1_1_NAME, 2022, ART1_ID);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        RowMapper<Track> rm = (rs, rowNum) -> new Track(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("year"),
                rs.getInt("artist_id"));
        List<Track> tracks = jdbcTemplate.query("SELECT * FROM tracks", rm);
        
        assertThat(tracks).contains(newTrack);
    }
    
    @Test
    public void putTrackBadTrackIdZero()
    {
        final Track newTrack = new Track(NEW_TRACK_NAME, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/0"));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No track with that id exists");
    }
    
    @Test
    public void putTrackBadTrackIdString()
    {
        final String trackId = "x";
        final Track newTrack = new Track(NEW_TRACK_NAME, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + trackId));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + trackId + "\"");
    }
    
    @Test
    public void putTrackDuplicateName()
    {
        final Track newTrack = new Track(TRK1_2_NAME, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode().value()).isEqualTo(409);
        assertThat(res.getBody()).isEqualTo("Track with that name already exists");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> tracks = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(tracks).containsOnlyOnce(newTrack.getName());
    }
    
    @Test
    public void putTrackBadArtistIdZero()
    {
        final Track newTrack = new Track(NEW_TRACK_NAME, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + 0 + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No artist with that id exists");
    }
    
    @Test
    public void putTrackBadArtistIdString()
    {
        final String artistId = "x";
        final Track newTrack = new Track(NEW_TRACK_NAME, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + artistId + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + artistId + "\"");
    }
    
    @Test
    public void putTrackEmptyName()
    {
        final Track newTrack = new Track("", 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot set empty name to track");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(trackNames).doesNotContain("");
    }
    
    @Test
    public void putTrackNullName()
    {
        final Track newTrack = new Track(null, 2022);
        RequestEntity<Track> req = new RequestEntity<>(newTrack, HttpMethod.PUT,
                                                       URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("Cannot set empty name to track");
        
        RowMapper<String> rm = (rs, rowNum) -> rs.getString("name");
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", rm);
        
        assertThat(trackNames).doesNotContain((String) null);
    }
    
    @Test
    public void deleteTrackIdOne()
    {
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE,
                                                        URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNull();
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (s, n) -> s.getString("name"));
        
        assertThat(trackNames).hasSize(TRACKS.size() - 1);
        assertThat(trackNames).doesNotContain(TRK1_1_NAME);
    }
    
    @Test
    public void deleteTrackWrongIdZero()
    {
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE,
                                                        URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/0"));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No track with that id exists");
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (s, n) -> s.getString("name"));
        
        assertThat(trackNames).hasSize(TRACKS.size());
    }
    
    @Test
    public void deleteTrackWrongIdString()
    {
        final String trackId = "x";
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE,
                                                        URI.create(getBaseUrl() + "artists/" + ART1_ID + "/tracks/" + trackId));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + trackId + "\"");
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (s, n) -> s.getString("name"));
        
        assertThat(trackNames).hasSize(TRACKS.size());
    }
    
    @Test
    public void deleteTrackWrongArtistIdZero()
    {
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE,
                                                        URI.create(getBaseUrl() + "artists/0/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).isEqualTo("No artist with that id exists");
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (s, n) -> s.getString("name"));
        
        assertThat(trackNames).hasSize(TRACKS.size());
    }
    
    @Test
    public void deleteTrackWrongArtistIdString()
    {
        final String artistId = "x";
        RequestEntity<Artist> req = new RequestEntity<>(HttpMethod.DELETE,
                                                        URI.create(getBaseUrl() + "artists/" + artistId + "/tracks/" + TRK1_1_ID));
        ResponseEntity<String> res = restTemplate.exchange(req, String.class);
        
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(res.getBody()).contains("Wrong path variable type: Failed to convert value of type 'java.lang.String' " +
                                                   "to required type 'java.lang.Integer'; For input string: \"" + artistId + "\"");
        
        List<String> trackNames = jdbcTemplate.query("SELECT name FROM tracks", (s, n) -> s.getString("name"));
        
        assertThat(trackNames).hasSize(TRACKS.size());
    }
}





















