package se.jensen.javacourse.week4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Track implements Serializable
{
    @JsonProperty("name")
    private String name;
    @JsonProperty("year")
    private int year;
    private int artistId;
    
    public Track(String name, int year)
    {
        this.name = name;
        this.year = year;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getYear()
    {
        return year;
    }
    
    public void setYear(int year)
    {
        this.year = year;
    }
    
    public int getArtistId()
    {
        return artistId;
    }
    
    void setArtistId(int artistId)
    {
        this.artistId = artistId;
    }
    
    @Override
    public String toString()
    {
        return "Track{" +
                "name='" + name + '\'' +
                ", artistId=" + artistId +
                ", year=" + year +
                '}';
    }
}
