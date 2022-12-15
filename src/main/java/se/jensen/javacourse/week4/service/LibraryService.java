package se.jensen.javacourse.week4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import se.jensen.javacourse.week4.database.LibraryRepository;
import se.jensen.javacourse.week4.model.Artist;
import se.jensen.javacourse.week4.model.Track;

@Service
public class LibraryService
{
    @Autowired
    LibraryRepository db;
    
    public HashMap<Integer, Artist> getArtists()
    {
        return db.readArtists();
    }
    
    public List<String> getArtistsNamesOnly()
    {
        return db.readArtistNames();
    }
    
    public Object getArtistById(int id)
    {
        return db.readArtistById(id);
    }
    
    public Object getArtistByName(String name)
    {
        return db.readArtistByName(name);
    }
    
    /** Creates a new artist and returns 1 if no errors.
     * If the name is null or empty, returns -2.
     * If another artist already has that name, returns -1. */
    public int createArtist(Artist artist)
    {
        if (artist.getName() == null || artist.getName().isEmpty())
            return -2;
        return db.insertArtist(artist.getName().trim());
    }
    
    /** Adds a new track to a specific artist and returns 1.
     * If the track name is null or empty, returns -3.
     * If no artist exists with that id, returns -2.
     * If the artist already has a track with that name, returns -1. */
    public int addTrack(Integer artistId, Track track)
    {
        if (track.getName() == null || track.getName().isEmpty())
            return -3;
        if (db.readArtistById(artistId) == null)
            return -2;
        return db.insertTrack(artistId, track);
    }
    
    public int updateArtist(int id, Artist artist)
    {
        if (artist.getName() == null || artist.getName().isEmpty())
            return -3;
        return db.updateArtist(id, artist);
    }
    
    public int updateTrack(int artistId, int trackId, Track track)
    {
        if (track.getName() == null || track.getName().isEmpty())
            return -3;
        if (db.readArtistById(artistId) == null) return -2;
        return db.updateTrack(artistId, trackId, track);
    }
    
    public int deleteArtist(int id)
    {
        return db.deleteArtist(id);
    }
    
    /** Deletes an existing track of a specific artist
     *  and returns 1 if no errors.
     * If no artist exists with that id, returns -2. */
    public int deleteTrack(int artistId, int trackId)
    {
        if (db.readArtistById(artistId) == null) return -2;
        return db.deleteTrack(artistId, trackId);
    }
    
    public List<Track> getTracks()
    {
        return db.readTracks();
    }
    
    public Object getTrack(int artistId, int trackId)
    {
        if (db.readArtistById(artistId) == null) return -2;
        return db.readTrack(trackId, artistId);
    }
}