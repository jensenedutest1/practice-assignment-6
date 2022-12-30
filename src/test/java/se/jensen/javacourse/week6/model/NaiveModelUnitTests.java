package se.jensen.javacourse.week6.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Just some basic unit tests that make sure Artist.java and Track.java have everything they need and behave correctly.
 */
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
    }
    
    @Test
    public void artistConstructorTwoInitializesTracksAndSetsFields()
    {
    }
    
    @Test
    public void artistAddTrackAddsTrackAndArtistReferenceToTrack()
    {
    }
    
    @Test
    public void artistAddTrackWithIdAddsTrackUnderCorrectKey()
    {
    }
    
    // Track.java only has basic constructors and getters and setters,
    // so we're not gonna test those, although we could do that too,
    // to make sure that they stay unchanged
}
