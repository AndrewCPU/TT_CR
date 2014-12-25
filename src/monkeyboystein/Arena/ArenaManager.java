package monkeyboystein.Arena;

import monkeyboystein.Main;
import monkeyboystein.utils.Storage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
* Created by Andrewcpu on 12/24/14.
*/
public class ArenaManager {
    Storage storage = Main.storage;
    public boolean isPlayerInArena(Player p)
    {
        for(Arena a : storage.getArenas())
        {
            if(a.getPlayers().contains(p.getName()))
            {
                return true;
            }

        }
        return false;
    }
    public Arena getPlayerArena(Player p)
    {
        for(Arena a : storage.getArenas())
        {
            if(a.getPlayers().contains(p.getName()))
            {
                return a;
            }

        }
        return null;
    }

    public Arena getArenaByName(String name)
    {
        for(Arena a : storage.getArenas())
        {
            if(a.getName().equalsIgnoreCase(name))
            {
                return a;
            }
        }
        return null;
    }

    public Team getPlayerTeam(Player p)
    {
        for(Arena a : storage.getArenas())
        {
            if(a.getPlayers().contains(p.getName()))
            {
                if(a.getWhite().getPlayers().contains(p.getName()))
                {
                    return a.getWhite();
                }
                else if(a.getBlack().getPlayers().contains(p.getName()))
                {
                    return a.getBlack();
                }
                else if(a.getRed().getPlayers().contains(p.getName()))
                {
                    return a.getRed();
                }
            }
        }
        return null;
    }

    public Arena createArena(ArenaSpawnManager arenaSpawnManager, Location lobby, int maxTime, String name)
    {
        Arena a = new Arena(arenaSpawnManager,lobby,maxTime,name);
        return a;
    }

    public void updateArena(Arena a)
    {

        if(storage.getArenas().contains(a))
        {
            for(Arena arena : storage.getArenas())
            {
                if(arena.getName().equalsIgnoreCase(a.getName()))
                {
                    arena.endGame();
                }
            }
            storage.getArenas().remove(a);
            storage.getArenas().add(createArena(a.getArenaSpawnManager(),a.getLobby(),storage.getMaxPlayers(),a.getName()));
        }

    }
}
