package se.jensen.javacourse.week6.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NaiveModelUnitTests
{
    static final int artistId = 1;
    static final String artistName = "Test Artist";
    static final int trackId = 2;
    static final int trackYear = 2000;
    static final String trackName = "Test Track";
    
    @Test
    public void artistEmptyConstructorInitializesTracks()
    {
        Artist artist = new Artist();
        assertThat(artist.tracks).isNotNull();
    }
    
    @Test
    public void artistConstructorTwoInitializesTracksAndSetsFields()
    {
        Artist artist = new Artist(artistId, artistName);
        assertThat(artist.tracks).isNotNull();
        assertThat(artist.getId()).isEqualTo(artistId);
        assertThat(artist.getName()).isEqualTo(artistName);
    }
    
    @Test
    public void artistAddTrackAddsTrackAndArtistReferenceToTrack()
    {
        Artist artist = new Artist(artistId, artistName);
        Track track = new Track(trackName, trackYear);
        artist.addTrack(track);
        assertThat(artist.tracks).containsValue(track);
        assertThat(track.getArtistId()).isEqualTo(artistId);
    }
    
    @Test
    public void artistAddTrackWithIdAddsTrackUnderCorrectKey()
    {
        Artist artist = new Artist(artistId, artistName);
        Track track = new Track(trackId, trackName, trackYear);
        artist.addTrack(track);
        assertThat(artist.tracks.get(trackId)).isEqualTo(track);
    }
    
    // Track.java only has basic constructors and getters and setters,
    // so we're not gonna test those, although we could do that too,
    // to make sure that they stay unchanged
}

















