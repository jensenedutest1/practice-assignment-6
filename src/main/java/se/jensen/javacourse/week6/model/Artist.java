package se.jensen.javacourse.week6.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

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
    
    @Override
    public String toString()
    {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tracks=" + tracks +
                '}';
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Artist artist = (Artist) o;
        return id == artist.id && Objects.equals(name, artist.name) && Objects.equals(tracks, artist.tracks);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, tracks);
    }
    //    @Override
//    public boolean equals(Object o)
//    {
//        if (this == o)
//            return true;
//        if (o == null || getClass() != o.getClass())
//            return false;
//        Artist artist = (Artist) o;
//        return id == artist.id && name.equals(artist.name) && tracks.equals(artist.tracks);
//    }

//    @Override
//    public int hashCode()
//    {
//        return Objects.hash(id, name, tracks);
//    }
}
