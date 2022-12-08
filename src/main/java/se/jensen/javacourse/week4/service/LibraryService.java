package se.jensen.javacourse.week4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se.jensen.javacourse.week4.database.LibraryRepository;
import se.jensen.javacourse.week4.model.Artist;
import se.jensen.javacourse.week4.model.Track;

@Service
public class LibraryService
{
    @Autowired
    LibraryRepository db;
    
    public Map<Integer, Artist> getArtists()
    {
        return db.readArtists();
    }
    
    public List<String> getArtistsNamesOnly()
    {
        List<String> artistNames = new ArrayList<>();
        for (Artist artist : db.readArtists().values())
        {
            artistNames.add(artist.getName());
        }
        return artistNames;
    }
    
    /** Creates a new artist and returns 0 if no errors.
     * If the name is null or empty, returns -2.
     * If another artist already has that name, returns -1. */
    public int createArtist(Artist newArtist)
    {
        if (newArtist.getName() == null || newArtist.getName().isEmpty())
            return -2;
        
        if (db.readArtistByName(newArtist.getName()) != null) return -1;
        
        Artist _newArtist = new Artist(newArtist.getName());
        _newArtist.setId(db.lastI);
        db.saveArtist(db.lastI++, _newArtist);
        return 0;
    }
    
    /** Adds a new track to a specific artist and returns 0 if no errors.
     * If the track name is null or empty, returns -3.
     * If no artist exists with that id, returns -2.
     * If the artist already has a track with that name, returns -1. */
    public int addTrack(Integer artistId, Track track)
    {
        String trackName = track.getName();
        if (trackName == null || trackName.isEmpty()) return -3;
        
        Artist artist = db.readArtistById(artistId);
        if (artist == null) return -2;
        
        if (artist.hasTrack(trackName)) return -1;
        
        artist.addTrack(track);
        db.saveArtist(artistId, artist);
        return 0;
    }
    
    /** Updates the name of a specific artist, unless it's empty,
     *  and returns 0 if no errors.
     * If no artist exists with that id, returns -2.
     * If another artist already has that name, returns -1. */
    public int updateArtist(Integer id, Artist newArtist)
    {
        Artist oldArtist = db.readArtistById(id);
        if (oldArtist == null) return -2;
        Artist sameNameArtist = db.readArtistByName(newArtist.getName());
        if (sameNameArtist != null && sameNameArtist.getId() != oldArtist.getId())
            return -1;
        oldArtist.update(newArtist);
        db.saveArtist(id, oldArtist);
        return 0;
    }
    
    /** Updates an existing track of an existing artist
     *  and returns 0 if no errors.
     * If no artist exists with that id, returns -2.
     * If the artist already has another track with that name, returns -1. */
    public int updateTrack(Integer artistId, Integer trackId, Track track)
    {
        Artist artist = db.readArtistById(artistId);
        if (artist == null) return -2;
        
        if (artist.hasTrack(track.getName(), trackId)) return -1;
        
        int res = artist.updateTrack(trackId, track);
        if (res == 0)
            db.saveArtist(artistId, artist);
        return res;
    }
    
    public void deleteArtist(Integer id)
    {
        db.removeArtist(id);
    }
    
    /** Deletes an existing track of a specific artist
     *  and returns 0 if no errors.
     * If no artist exists with that id, returns -2.
     * If no track exists with that id, returns -3. */
    public int deleteTrack(Integer artistId, Integer trackId)
    {
        Artist artist = db.readArtistById(artistId);
        if (artist == null) return -2;
    
        int res = artist.deleteTrack(trackId);
        if (res == 0)
            db.saveArtist(artistId, artist);
        return res;
    }
    
    public List<Track> getTracks()
    {
        return db.readTracks();
    }
}