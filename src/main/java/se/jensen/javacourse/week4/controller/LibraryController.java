package se.jensen.javacourse.week4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import se.jensen.javacourse.week4.model.Artist;
import se.jensen.javacourse.week4.model.Track;
import se.jensen.javacourse.week4.service.LibraryService;

@RequestMapping("/api/v1")
@RestController
public class LibraryController
{
    @Autowired
    LibraryService libraryService;
    
    @GetMapping("/artists")
    public Object getArtists(HttpServletRequest req)
    {
        boolean namesOnly = "true".equals(req.getParameter("namesOnly"));
        if (namesOnly)
            return libraryService.getArtistsNamesOnly();
        return libraryService.getArtists();
    }
    
    @GetMapping("/tracks")
    public List<Track> getTracks()
    {
        return libraryService.getTracks();
    }
    
    @PostMapping("/artists")
    public ResponseEntity postArtist(@RequestBody Artist artist)
    {
        int res = libraryService.createArtist(artist);
        switch (res)
        {
            default:
            case 0:
                return new ResponseEntity(HttpStatus.CREATED);
            case -1:
                return new ResponseEntity("Artist with that name already exists", HttpStatus.FORBIDDEN);
            case -2:
                return new ResponseEntity("Cannot create artist with empty name", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/artists/{artistId}/tracks")
    public ResponseEntity postTrack(@PathVariable("artistId") Integer artistId,
                                    @RequestBody Track track)
    {
        int res = libraryService.addTrack(artistId, track);
        switch (res)
        {
            default:
            case 0:
                return new ResponseEntity(HttpStatus.CREATED);
            case -1:
                return new ResponseEntity("Track with that name already exists", HttpStatus.FORBIDDEN);
            case -2:
                return new ResponseEntity("No artist with that id exists", HttpStatus.BAD_REQUEST);
            case -3:
                return new ResponseEntity("Cannot create track with empty name", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/artists/{id}")
    public ResponseEntity putArtist(@PathVariable("id") Integer id, @RequestBody Artist artist)
    {
        int res = libraryService.updateArtist(id, artist);
        switch (res)
        {
            default:
            case 0:
                return new ResponseEntity(HttpStatus.OK);
            case -1:
                return new ResponseEntity("Another artist with that name already exists", HttpStatus.FORBIDDEN);
            case -2:
                return new ResponseEntity("No artist with that id exists", HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/artists/{artistId}/tracks/{trackId}")
    public ResponseEntity putTrack(@PathVariable("artistId") Integer artistId,
                                   @PathVariable("trackId") Integer trackId,
                                   @RequestBody Track track)
    {
        int res = libraryService.updateTrack(artistId, trackId, track);
        switch (res)
        {
            default:
            case 0:
                return new ResponseEntity(HttpStatus.OK);
            case -1:
                return new ResponseEntity("Track with that name already exists", HttpStatus.FORBIDDEN);
            case -2:
                return new ResponseEntity("No artist with that id exists", HttpStatus.BAD_REQUEST);
            case -3:
                return new ResponseEntity("No track with that id exists", HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/artists/{id}")
    public void deleteArtist(@PathVariable("id") Integer id)
    {
        libraryService.deleteArtist(id);
    }
    
    @DeleteMapping("/artists/{artistId}/tracks/{trackId}")
    public ResponseEntity deleteTrack(@PathVariable("artistId") Integer artistId,
                                      @PathVariable("trackId") Integer trackId)
    {
        int res = libraryService.deleteTrack(artistId, trackId);
        switch (res)
        {
            default:
            case 0:
                return new ResponseEntity(HttpStatus.OK);
            case -2:
                return new ResponseEntity("No artist with that id exists", HttpStatus.BAD_REQUEST);
            case -3:
                return new ResponseEntity("No track with that id exists", HttpStatus.OK);
        }
    }
}
