package monkeyboystein.Arena;

import org.bukkit.Location;

import java.util.HashMap;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class ArenaSpawnManager {
    HashMap<String,Location> spawns = new HashMap<String, Location>();
    public Location getSpawn(String i)
    {
        return spawns.get(i);
    }
    public void setSpawn(String i, Location loc)
    {
        if(spawns.containsKey(i))
        {
            spawns.remove(i);

        }
        spawns.put(i, loc);
    }

}
