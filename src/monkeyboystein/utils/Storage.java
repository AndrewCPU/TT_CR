package monkeyboystein.utils;

import monkeyboystein.Arena.Arena;
import monkeyboystein.Arena.ArenaManager;
import monkeyboystein.Main;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class Storage {
    private List<Arena> arenas = new ArrayList<Arena>();
    private ArenaManager arenaManager;
    private EventListener eventListener;
    private SignManager signManager;
    private TickManager tickManager;
    private Main main;
    private int maxPlayers = 9;
    private String header = ChatColor.BLUE + "[" + ChatColor.GRAY + "TobyRun" + ChatColor.BLUE + "]";
    private ScoreboardManager scoreboardManager;
    private SQLAPI sqlapi;
    private String database;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public SQLAPI getSqlapi() {
        return sqlapi;
    }

    public void setSqlapi(SQLAPI sqlapi) {
        this.sqlapi = sqlapi;
    }

    public LocationUtility getLocationUtility() {
        return locationUtility;
    }

    public void setLocationUtility(LocationUtility locationUtility) {
        this.locationUtility = locationUtility;
    }

    private LocationUtility locationUtility;

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public void setScoreboardManager(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public void setArenas(List<Arena> arenas) {
        this.arenas = arenas;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public void setSignManager(SignManager signManager) {
        this.signManager = signManager;
    }

    public TickManager getTickManager() {
        return tickManager;
    }

    public void setTickManager(TickManager tickManager) {
        this.tickManager = tickManager;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void addArena(Arena arena)
    {
        if(!arenas.contains(arena))
        {
            arenas.add(arena);
        }
    }

    public void removeArena(Arena arena)
    {
        if(arenas.contains(arena))
        {
            arenas.remove(arena);
        }
    }
}
