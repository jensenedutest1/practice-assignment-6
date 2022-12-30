package se.jensen.javacourse.week6;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week6.model.Artist;
import se.jensen.javacourse.week6.model.Track;

public class Tests
{
    protected final ObjectMapper OM = new ObjectMapper();
    protected final static int ART1_ID = 1;
    protected final static int ART2_ID = 2;
    protected final static int ART3_ID = 3;
    
    protected final static String ART1_NAME = "Led Zeppelin";
    protected final static String ART2_NAME = "Pink Floyd";
    protected final static String ART3_NAME = "The Black Keys";
    protected final static String NEW_ARTIST_NAME = "Greta Van Fleet";
    protected final static String NAME_DUPLICATE = "Duplicate";
    protected final static String NAME_NONEXISTENT = "Nonexistent";
    
    protected final static int TRK1_1_ID = 1;
    protected final static int TRK1_2_ID = 2;
    protected final static int TRK1_3_ID = 3;
    protected final static int TRK2_1_ID = 4;
    protected final static int TRK2_2_ID = 5;
    protected final static int TRK3_1_ID = 6;
    protected final static int TRK3_2_ID = 7;
    
    protected final static String TRK1_1_NAME = "Whole Lotta Love";
    protected final static String TRK1_2_NAME = "Immigrant Song";
    protected final static String TRK1_3_NAME = "Stairway to Heaven";
    protected final static String TRK2_1_NAME = "Wish You Were Here";
    protected final static String TRK2_2_NAME = "The Wall";
    protected final static String TRK3_1_NAME = "I Got Mine";
    protected final static String TRK3_2_NAME = "Howling for You";
    protected final static String NEW_TRACK_NAME = "Ramble On";
    
    protected final static int TRK1_1_YEAR = 1969;
    protected final static int TRK1_2_YEAR = 1970;
    protected final static int TRK1_3_YEAR = 1971;
    protected final static int TRK2_1_YEAR = 1975;
    protected final static int TRK2_2_YEAR = 1979;
    protected final static int TRK3_1_YEAR = 2008;
    protected final static int TRK3_2_YEAR = 2011;
    
    protected static Artist ART_1;
    protected static Artist ART_2;
    protected static Artist ART_3;
    protected final static Artist ARTIST_1_NO_TRACKS = new Artist(ART1_ID, ART1_NAME);
    protected final static Artist ARTIST_2_NO_TRACKS = new Artist(ART2_ID, ART2_NAME);
    protected final static Artist ARTIST_DUPLICATE = new Artist(ART1_ID, NAME_DUPLICATE);
    protected final static Artist ARTIST_EMPTY = new Artist(ART1_ID, "");
    protected final static Artist ARTIST_NULL = new Artist(ART1_ID, null);
    
    protected final static Track TRK1_1 = new Track(TRK1_1_ID, TRK1_1_NAME, TRK1_1_YEAR, ART1_ID);
    protected final static Track TRK1_2 = new Track(TRK1_2_ID, TRK1_2_NAME, TRK1_2_YEAR, ART1_ID);
    protected final static Track TRK1_3 = new Track(TRK1_3_ID, TRK1_3_NAME, TRK1_3_YEAR, ART1_ID);
    protected final static Track TRK2_1 = new Track(TRK2_1_ID, TRK2_1_NAME, TRK2_1_YEAR, ART2_ID);
    protected final static Track TRK2_2 = new Track(TRK2_2_ID, TRK2_2_NAME, TRK2_2_YEAR, ART2_ID);
    protected final static Track TRK3_1 = new Track(TRK3_1_ID, TRK3_1_NAME, TRK3_1_YEAR, ART3_ID);
    protected final static Track TRK3_2 = new Track(TRK3_2_ID, TRK3_2_NAME, TRK3_2_YEAR, ART3_ID);
    protected final static Track TRACK_DUPLICATE = new Track(NAME_DUPLICATE, TRK1_1_YEAR);
    protected final static Track TRACK_EMPTY = new Track("", TRK1_1_YEAR);
    protected final static Track TRACK_NULL = new Track(null, TRK1_1_YEAR);
    
    protected static HashMap<Integer, Artist> ARTISTS_MAP;
    protected static List<String> ARTIST_NAMES;
    protected static List<Track> TRACKS;
    
    @BeforeAll
    protected static void beforeAll() throws NoSuchFieldException, IllegalAccessException
    {
        ARTISTS_MAP = new HashMap<>();
        ART_1 = new Artist(ART1_ID, ART1_NAME);
        ART_1.addTrack(TRK1_1);
        ART_1.addTrack(TRK1_2);
        ART_1.addTrack(TRK1_3);
        ART_2 = new Artist(ART2_ID, ART2_NAME);
        ART_2.addTrack(TRK2_1);
        ART_2.addTrack(TRK2_2);
        ART_3 = new Artist(ART3_ID, ART3_NAME);
        ART_3.addTrack(TRK3_1);
        ART_3.addTrack(TRK3_2);
        ARTISTS_MAP.put(ART1_ID, ART_1);
        ARTISTS_MAP.put(ART2_ID, ART_2);
        ARTISTS_MAP.put(ART3_ID, ART_3);
        
        ARTIST_NAMES = new ArrayList<>();
        ARTIST_NAMES.add(ART1_NAME);
        ARTIST_NAMES.add(ART2_NAME);
        ARTIST_NAMES.add(ART3_NAME);
        
        TRACKS = new ArrayList<>();
        TRACKS.addAll(ART_1.tracks.values());
        TRACKS.addAll(ART_2.tracks.values());
        TRACKS.addAll(ART_3.tracks.values());
    }
}
