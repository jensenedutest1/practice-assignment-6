package se.jensen.javacourse.week4.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
        String name = req.getParameter("name");
        if (name != null && !name.isEmpty())
            return libraryService.getArtistByName(name);
        boolean namesOnly = "true".equals(req.getParameter("namesOnly"));
        if (namesOnly)
            return libraryService.getArtistsNamesOnly();
        return libraryService.getArtists();
    }
    
    @GetMapping("/artists/{id}")
    public Object getArtist(@PathVariable("id") int id)
    {
        return libraryService.getArtistById(id);
    }
    
    @PostMapping("/artists")
    public ResponseEntity<String> postArtist(@RequestBody Artist artist)
    {
        int res = libraryService.createArtist(artist);
        switch (res)
        {
            case 1:
                return ResponseEntity.status(201).build();
            case -1:
                return ResponseEntity.status(403).body("Artist with that name already exists");
            case -2:
                return ResponseEntity.badRequest().body("Cannot create artist with empty name");
            default:
                return ResponseEntity.internalServerError().body("Server error, probably");
        }
    }
    
    @PutMapping("/artists/{id}")
    public ResponseEntity<String> putArtist(@PathVariable("id") Integer id, @RequestBody Artist artist)
    {
        int res = libraryService.updateArtist(id, artist);
        switch (res)
        {
            case 0:
                return ResponseEntity.badRequest().body("No artist with that id exists");
            case 1:
                return ResponseEntity.ok().build();
            case -1:
                return ResponseEntity.status(403).body("Another artist with that name already exists");
            case -3:
                return ResponseEntity.badRequest().body("Cannot set empty name to artist");
            default:
                return ResponseEntity.internalServerError().body("Server error, probably");
        }
    }
    
    @DeleteMapping("/artists/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable("id") Integer id)
    {
        if (libraryService.deleteArtist(id) >= 0)
            return ResponseEntity.ok().build();
        return ResponseEntity.internalServerError().build();
    }
    
    @GetMapping("/tracks")
    public List<Track> getTracks()
    {
        return libraryService.getTracks();
    }
    
    @GetMapping("/artists/{artistId}/tracks/{trackId}")
    public ResponseEntity<Object> getTracks(@PathVariable("artistId") Integer artistId,
                                 @PathVariable("trackId") Integer trackId)
    {
        Object res = libraryService.getTrack(artistId, trackId);
        if (res instanceof Integer && (int) res == -2)
            return ResponseEntity.badRequest().body("No artist with that id exists");
        return ResponseEntity.ok().body(libraryService.getTrack(artistId, trackId));
    }
    
    @PostMapping("/artists/{artistId}/tracks")
    public ResponseEntity<String> postTrack(@PathVariable("artistId") Integer artistId,
                                    @RequestBody Track track)
    {
        int res = libraryService.addTrack(artistId, track);
        switch (res)
        {
            case 1:
                return ResponseEntity.status(201).build();
            case -1:
                return ResponseEntity.status(403).body("Track with that name already exists");
            case -2:
                return ResponseEntity.badRequest().body("No artist with that id exists");
            case -3:
                return ResponseEntity.badRequest().body("Cannot create track with empty name");
            default:
                return ResponseEntity.internalServerError().body("Server error, probably");
        }
    }
    
    @PutMapping("/artists/{artistId}/tracks/{trackId}")
    public ResponseEntity<String> putTrack(@PathVariable("artistId") Integer artistId,
                                           @PathVariable("trackId") Integer trackId,
                                           @RequestBody Track track)
    {
        int res = libraryService.updateTrack(artistId, trackId, track);
        switch (res)
        {
            case 0:
                return ResponseEntity.badRequest().body("No track with that id exists");
            case 1:
                return ResponseEntity.ok().build();
            case -1:
                return ResponseEntity.status(403).body("Track with that name already exists");
            case -2:
                return ResponseEntity.badRequest().body("No artist with that id exists");
            case -3:
                return ResponseEntity.badRequest().body("Cannot set empty name to track");
            default:
                return ResponseEntity.internalServerError().body("Server error, probably");
        }
    }
    
    @DeleteMapping("/artists/{artistId}/tracks/{trackId}")
    public ResponseEntity<String> deleteTrack(@PathVariable("artistId") Integer artistId,
                                      @PathVariable("trackId") Integer trackId)
    {
        int res = libraryService.deleteTrack(artistId, trackId);
        switch (res)
        {
            default:
            case 1:
                return ResponseEntity.ok().build();
            case -2:
                return ResponseEntity.badRequest().body("No artist with that id exists");
            case -3:
                return ResponseEntity.badRequest().body("No track with that id exists");
        }
    }
}
