package se.jensen.javacourse.week4.model;

import java.io.Serializable;
import java.util.HashMap;

public class TrackList extends HashMap<Integer, Track> implements Serializable
{
    public Integer lastI;
    
    public TrackList()
    {
        this.lastI = 0;
    }
    
    public void add(Track track)
    {
        put(lastI++, track);
    }
    
    public boolean containsName(String trackName)
    {
        for (Track track : values())
        {
            if (track.getName().equals(trackName))
                return true;
        }
        return false;
    }
    
    public Track getByName(String trackName)
    {
        for (Track track : values())
        {
            if (track.getName().equals(trackName))
                return track;
        }
        return null;
    }
}
