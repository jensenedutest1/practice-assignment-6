package se.jensen.javacourse.week4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;

public class Artist implements Serializable
{
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    public final HashMap<Integer, Track> tracks;
    
    public Artist()
    {
        this.tracks = new HashMap<>();
    }
    
    public Artist(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.tracks = new HashMap<>();
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void addTrack(Track track)
    {
        track.setArtistId(id);
        tracks.put(track.getId(), track);
    }
}
