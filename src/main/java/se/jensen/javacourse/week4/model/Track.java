package se.jensen.javacourse.week4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Track implements Serializable
{
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("year") private int year;
    @JsonProperty("artistId") private int artistId;
    
    public Track() {}
    
    public Track(String name, int year)
    {
        this.name = name;
        this.year = year;
    }
    public Track(int id, String name, int year)
    {
        this.id = id;
        this.name = name;
        this.year = year;
    }
    
    public Track(int id, String name, int year, int artistId)
    {
        this.id = id;
        this.name = name;
        this.year = year;
        this.artistId = artistId;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getYear()
    {
        return year;
    }
    
    public int getArtistId()
    {
        return artistId;
    }
    
    void setArtistId(int artistId)
    {
        this.artistId = artistId;
    }
}
