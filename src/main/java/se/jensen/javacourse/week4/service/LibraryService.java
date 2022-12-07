package se.jensen.javacourse.week4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    public int addTrack(Integer artistId, Track track)
    {
        String trackName = track.getName();
        if (trackName == null || trackName.isEmpty()) return -3;
        
        Artist artist = db.readArtistById(artistId);
        if (artist == null) return -2;
        
        if (artist.hasTrack(trackName)) return -1;
        
//        track.setArtist(artist);
        artist.addTrack(track);
        db.saveArtist(artistId, artist);
        return 0;
    }
    
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
    
    public int updateTrack(Integer artistId, Integer trackId, Track track)
    {
        Artist artist = db.readArtistById(artistId);
        if (artist == null) return -2;
        
        if (artist.hasTrack(track.getName(), trackId)) return -1;
        
//        track.setArtist(artist);
        int res = artist.updateTrack(trackId, track);
        if (res == 0)
            db.saveArtist(artistId, artist);
        return res;
    }
    
    public void deleteArtist(Integer id)
    {
        db.removeArtist(id);
    }
    
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