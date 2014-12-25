package monkeyboystein.Arena;

import monkeyboystein.Main;
import monkeyboystein.utils.BlockMemory;
import monkeyboystein.utils.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class Arena {
    private Storage storage = Main.storage;
    private String name;
    private int maxTime;
    private Location lobby;
    private int time;
    private ArenaSpawnManager arenaSpawnManager;
    private Team red = new Team("RED");
    private Team black = new Team("BLACK");
    private Team white = new Team("WHITE");
    private BlockMemory memory = new BlockMemory();
    private ArenaState state = ArenaState.OFF;
    private JoinState joinState = JoinState.JOINABLE;
    public List<Location> changedBlock = new ArrayList<Location>();
    private Location signLoc;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public List<Location> getChangedBlock() {
        return changedBlock;
    }

    public void setChangedBlock(List<Location> changedBlock) {
        this.changedBlock = changedBlock;
    }

    public Location getSignLoc() {
        return signLoc;
    }

    public void setSignLoc(Location signLoc) {
        this.signLoc = signLoc;
    }

    public int getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(int countdownTime) {
        this.countdownTime = countdownTime;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public JoinState getJoinState() {
        return joinState;
    }

    public void setJoinState(JoinState joinState) {
        this.joinState = joinState;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    private List<String> players = new ArrayList<String>();

    public BlockMemory getMemory() {
        return memory;
    }

    public void setMemory(BlockMemory memory) {
        this.memory = memory;
    }

    public Arena(ArenaSpawnManager arenaSpawnManager, Location lobby, int maxTime, String name) {
        this.arenaSpawnManager = arenaSpawnManager;
        this.lobby = lobby;
        this.maxTime = maxTime;
        this.name = name;
        setSignLoc(new Location(Bukkit.getWorld("world"), storage.getMain().getConfig().getInt(getName() + ".Sign.X"), storage.getMain().getConfig().getInt(getName() + ".Sign.Y"), storage.getMain().getConfig().getInt(getName() + ".Sign.Z")));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArenaSpawnManager getArenaSpawnManager() {
        return arenaSpawnManager;
    }

    public void setArenaSpawnManager(ArenaSpawnManager arenaSpawnManager) {
        this.arenaSpawnManager = arenaSpawnManager;
    }

    public void removePlayer(String s)
    {
        if(getPlayers().contains(s))
        {
            players.remove(s);
        }
    }

    public void addPlayer(String s)
    {
        if(!getPlayers().contains(s))
        {
            players.add(s);
        }
    }

    public Team getRed() {
        return red;
    }

    public void setRed(Team red) {
        this.red = red;
    }

    public Team getBlack() {
        return black;
    }

    public void setBlack(Team black) {
        this.black = black;
    }

    public Team getWhite() {
        return white;
    }

    public void setWhite(Team white) {
        this.white = white;
    }

    public List<String> getAllPlayers()
    {
        List<String> players = new ArrayList<String>();
        for(String s : white.getPlayers())
        {
        //    Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("WHITE"));
            players.add(s);
        }
        for(String s : black.getPlayers())
        {
       //     Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("BLACK"));
            players.add(s);
        }
        for(String s : red.getPlayers())
        {
        //    Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("RED"));
            players.add(s);
        }
        return players;
    }


    public void startGame()
    {
        ItemStack knockStick = new ItemStack(Material.STICK, 1);
        ItemMeta kMeta = knockStick.getItemMeta();
        kMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "MAGIC  KNOCKBACK  STICK");
        knockStick.setItemMeta(kMeta);
        for(String s : white.getPlayers())
        {
            Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("WHITE"));
            Bukkit.getPlayer(s).getInventory().addItem(knockStick);
        }
        for(String s : black.getPlayers())
        {
            Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("BLACK"));
            Bukkit.getPlayer(s).getInventory().addItem(knockStick);
        }
        for(String s : red.getPlayers())
        {
            Bukkit.getPlayer(s).teleport(arenaSpawnManager.getSpawn("RED"));
            Bukkit.getPlayer(s).getInventory().addItem(knockStick);
        }


    }
    public void endGame()
    {

        for(Location loc : changedBlock)
        {
            loc.getBlock().setType(Material.WOOD);
            loc.getBlock().setData((byte)8);
        }

        int redScore = red.getScore();
        int blackScore = black.getScore();
        int whiteScore = white.getScore();
        Team winner = null;
        if(redScore>=blackScore && redScore>=whiteScore)
        {
            winner = getRed();
        }
        if(whiteScore>=blackScore && whiteScore>=redScore)
        {
            winner = getWhite();
        }
        if(blackScore>=whiteScore && blackScore>=redScore)
        {
            winner = getBlack();
        }



        for(String p : getAllPlayers())
        {
            Bukkit.getPlayer(p).teleport(Bukkit.getWorld("world").getSpawnLocation());
            Bukkit.getPlayer(p).getInventory().setContents(Bukkit.createInventory(null,Bukkit.getPlayer(p).getInventory().getSize()).getContents());
            Bukkit.getPlayer(p).getEquipment().setArmorContents(null);
            if(winner==null)
            {
                Bukkit.getPlayer(p).sendMessage(storage.getHeader() + winner.getName() + " has won!");
            }

        }



        storage.getArenaManager().updateArena(this);

    }

    /*


     */
    private int countdownTime = 10;
    /*

     */
    public void tick()
    {

        if(getPlayers().size()==(1) && getState()!=ArenaState.ON)
        {
            setState(ArenaState.STARTING);
            countdownTime--;
            if(countdownTime==0)
            {
                setState(ArenaState.ON);
                startGame();
                countdownTime = 10;
            }
            else
            {
                for(String s : getPlayers())
                {
                    Bukkit.getPlayer(s).sendMessage(storage.getHeader() + "Starting in " + countdownTime);
                }
            }
        }
        else
        {

            countdownTime = 10;
            if(getState()==ArenaState.ON)
            {
                storage.getScoreboardManager().updateScoreboard(this);
                time--;
                for(String p : getPlayers())
                {
                    Bukkit.getPlayer(p).setLevel(time);
                }
                if(time<=0)
                {
                    setState(ArenaState.OFF);
                    endGame();
                }
            }
        }
    }

}
