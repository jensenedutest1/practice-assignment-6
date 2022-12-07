package se.jensen.javacourse.week4.database;

import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.jensen.javacourse.week4.model.Artist;
import se.jensen.javacourse.week4.model.Track;

// NOTE: this is not a real repository, it doesn't use SQL and doesn't make queries
@Repository
public class LibraryRepository
{
    private static final String DB_PATH = "src/main/resources/db.dat";
    
    public int lastI = 0;
    private Map<Integer, Artist> artistMap = new HashMap<>();
    
    public void writeToDisk()
    {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try
        {
            fout = new FileOutputStream(DB_PATH);
            oos = new ObjectOutputStream(fout);
            Map<String, Object> saveMap = new HashMap<>();
            saveMap.put("lastI", lastI);
            saveMap.put("artistMap", artistMap);
            oos.writeObject(saveMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fout.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                oos.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void readFromDisk()
    {
        ObjectInputStream objectinputstream = null;
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(DB_PATH);
            objectinputstream = new ObjectInputStream(fis);
            HashMap<Integer, Artist> artists;
            Map<String, Object> readMap = (HashMap<String, Object>) objectinputstream.readObject();
            artists = (HashMap<Integer, Artist>) readMap.get("artistMap");
            if (artists != null)
            {
                artistMap = artists;
                lastI = (Integer) readMap.get("lastI");
            }
        }
        catch (Exception e)
        {
//            e.printStackTrace();
        }
        finally
        {
            try
            {
                objectinputstream.close();
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
        }
    }
    
    public void saveArtist(int id, Artist artist)
    {
        artistMap.put(id, artist);
        writeToDisk();
    }
    
    public Map<Integer, Artist> readArtists()
    {
        return artistMap;
    }
    
    public Artist readArtistById(Integer id)
    {
        return artistMap.get(id);
    }
    
    public Artist readArtistByName(String name)
    {
        if (name == null) return null;
        for (Artist artist : artistMap.values())
        {
            if (artist.getName().equals(name))
                return artist;
        }
        return null;
    }
    
    public void removeArtist(Integer id)
    {
        artistMap.remove(id);
        writeToDisk();
    }
    
    public List<Track> readTracks()
    {
        List<Track> tracks = new ArrayList<>();
        for (Artist artist : artistMap.values())
        {
            tracks.addAll(artist.getTracks().values());
        }
        return tracks;
    }
}
