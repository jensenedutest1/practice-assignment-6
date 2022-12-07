package se.jensen.javacourse.week4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Artist implements Serializable
{
    private int id;
    @JsonProperty("name")
    private String name;
    private final TrackList tracks;
    
    public Artist()
    {
        this.tracks = new TrackList();
    }
    
    public Artist(String name)
    {
        this.name = name;
        this.tracks = new TrackList();
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public HashMap<Integer, Track> getTracks()
    {
        return tracks;
    }
    
    public void addTrack(Track track)
    {
        track.setArtistId(id);
        tracks.add(track);
        System.out.println("track = " + track);
        System.out.println("tracks = " + tracks);
    }
    
    public void update(Artist artist)
    {
        if (artist.name != null && !artist.name.isEmpty())
        {
            this.name = artist.name;
        }
    }
    
    public int updateTrack(int trackId, Track track)
    {
        Track oldTrack = tracks.get(trackId);
        if (oldTrack == null) return -3;
        
        String trackName = track.getName();
        if (trackName != null && !trackName.isEmpty())
            oldTrack.setName(track.getName());
        if (track.getYear() > 0)
            oldTrack.setYear(track.getYear());
        tracks.put(trackId, oldTrack);
        return 0;
    }
    
    public int deleteTrack(Integer trackId)
    {
        Track oldTrack = tracks.get(trackId);
        if (oldTrack == null) return -3;
        
        tracks.remove(trackId);
        return 0;
    }
    
    public boolean hasTrack(String trackName)
    {
        return tracks.containsName(trackName);
    }
    
    public boolean hasTrack(String trackName, int trackId)
    {
        Track track = tracks.getByName(trackName);
        if (track == null) return false;
        return !track.equals(tracks.get(trackId));
    }
    
    @Override
    public String toString()
    {
        String str = "\nArtist{" +
                "name='" + name + '\'' +
                ", tracks=\n";
        for (Track track : tracks.values())
        {
            str += "\t" + track.toString() + "\n";
        }
        str += '}';
        return str;
    }
}
