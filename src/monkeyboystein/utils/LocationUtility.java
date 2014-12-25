package monkeyboystein.utils;

import monkeyboystein.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class LocationUtility {
    Storage storage = Main.storage;
    public Location getTeamLocation(String arenaName, String teamName)
    {
        String path = arenaName.toUpperCase() + "." + teamName.toUpperCase() + ".";
        int x = storage.getMain().getConfig().getInt(path + "X");
        int y = storage.getMain().getConfig().getInt(path + "Y");
        int z = storage.getMain().getConfig().getInt(path + "Z");
        Location location = new Location(Bukkit.getWorld("world"),x,y,z);
        return location;
    }

    public Location getLobbyLocation(String arenaName)
    {
        String path = arenaName.toUpperCase() + "." + "Lobby".toUpperCase() + ".";
        int x = storage.getMain().getConfig().getInt(path + "X");
        int y = storage.getMain().getConfig().getInt(path + "Y");
        int z = storage.getMain().getConfig().getInt(path + "Z");
        Location location = new Location(Bukkit.getWorld("world"),x,y,z);
        return location;
    }



}
