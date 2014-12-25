package monkeyboystein.utils;

import monkeyboystein.Arena.Arena;
import monkeyboystein.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class TickManager {
    private Storage storage = Main.storage;
    private BukkitTask ticker;

    public TickManager()
    {
        ticker = new BukkitRunnable() {
            @Override
            public void run() {
                for(Arena a : storage.getArenas())
                {
                    a.tick();
                }
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(!(storage.getArenaManager().isPlayerInArena(p)))
                    {
                        storage.getScoreboardManager().updateMain(p);
                    }
                }

            }
        }.runTaskTimer(Main.storage.getMain(), 20, 20);
    }
    public void pauseTicker()
    {
        ticker.cancel();
    }
    public void startTicker()
    {
        ticker = new BukkitRunnable() {
            @Override
            public void run() {
                for(Arena a : storage.getArenas())
                {
                    a.tick();
                }
            }
        }.runTaskTimer(Main.storage.getMain(), 20, 20);
    }
}
