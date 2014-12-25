package monkeyboystein;

import monkeyboystein.Arena.Arena;
import monkeyboystein.Arena.ArenaManager;
import monkeyboystein.Arena.ArenaSpawnManager;
import monkeyboystein.Arena.ArenaState;
import monkeyboystein.Party.Party;
import monkeyboystein.Party.PartyManager;
import monkeyboystein.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class Main extends JavaPlugin {
    EventListener eventListener = new EventListener();
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(eventListener,this);
        writeArenas();
        saveDefaultConfig();
        setUp();
    }
    public static Storage storage = new Storage();

    public void setUp()
    {

        String url = (getConfig().getString("URL"));
        String user = (getConfig().getString("Username"));
        String pass = (getConfig().getString("Password"));
        String db = (getConfig().getString("Database"));
        int port = (getConfig().getInt("Port"));

        storage.setArenaManager(new ArenaManager());
        storage.setArenas(new ArrayList<Arena>());
        storage.setEventListener(eventListener);
        storage.setMain(this);
        storage.setSignManager(new SignManager());
        storage.setTickManager(new TickManager());
        storage.setScoreboardManager(new ScoreboardManager());
        storage.setLocationUtility(new LocationUtility());
        storage.setSqlapi(new SQLAPI(url, user, pass, port, db));
        storage.setDatabase(getConfig().getString("Database"));
    }
    List<String> games = getConfig().getStringList("Games");
    public List<Party> parties = new ArrayList<Party>();

    public Arena createArena(String s)
    {
        ArenaSpawnManager arenaSpawnManager = new ArenaSpawnManager();
        arenaSpawnManager.setSpawn("RED", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"RED"));
        arenaSpawnManager.setSpawn("WHITE", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"WHITE"));
        arenaSpawnManager.setSpawn("BLACK", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"BLACK"));

        Arena a = storage.getArenaManager().createArena(arenaSpawnManager, storage.getLocationUtility().getLobbyLocation(s.toUpperCase()), 400, s.toUpperCase());
        return a;
    }

    public void writeArenas()
    {
        for(String s : games)
        {
            ArenaSpawnManager arenaSpawnManager = new ArenaSpawnManager();
            arenaSpawnManager.setSpawn("RED", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"RED"));
            arenaSpawnManager.setSpawn("WHITE", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"WHITE"));
            arenaSpawnManager.setSpawn("BLACK", storage.getLocationUtility().getTeamLocation(s.toUpperCase(),"BLACK"));

            Arena a = storage.getArenaManager().createArena(arenaSpawnManager, storage.getLocationUtility().getLobbyLocation(s.toUpperCase()), 400, s.toUpperCase());
            storage.addArena(a);
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("party"))
        {
            String header = ChatColor.GREEN + "Party - ";
            Player p = (Player)sender;
            if(PartyManager.isInParty(p.getName()))
            {

                if(PartyManager.getParty(p.getName()).getOwner().equalsIgnoreCase(p.getName()))
                {
                    Party party = PartyManager.getParty(p.getName());
                    if(args.length==0)
                    {
                        p.sendMessage(header + "--------------------");
                        p.sendMessage(header + "party removeplayer [playername]");
                        p.sendMessage(header + "party invite [playername]");
                        p.sendMessage(header + "party setowner [playername]");
                        p.sendMessage(header + "--------------------");
                        return true;
                    }
                    else
                    {
                        if(args[0].equalsIgnoreCase("removeplayer"))
                        {

                            if(party.getMembers().contains(Bukkit.getPlayer(args[1]).getName()))
                            {
                                Player removed = Bukkit.getPlayer(args[1]);
                                removed.sendMessage(header + "You were removed from the party");
                                party.removeMember(removed.getName());
                                p.sendMessage(header + "You removed " + removed.getName());
                                return true;
                            }
                            else
                            {
                                p.sendMessage(header + "That player isnt in your party!");
                                return true;
                            }
                        }
                        else if(args[0].equalsIgnoreCase("invite"))
                        {
                            String playerName = Bukkit.getPlayer(args[1]).getName();
                            if(party.getMembers().contains(playerName))
                            {
                                //no
                                p.sendMessage(header + "Theyre already in your party");
                                return true;
                            }
                            else if(party.getInvites().contains(playerName))
                            {
                                //no
                                p.sendMessage(header + "You already invited them");
                                return true;
                            }
                            else
                            {
                                //yes
                                p.sendMessage(header + "Invitation sent!");
                                Bukkit.getPlayer(playerName).sendMessage(header + "You were invited to " + ChatColor.RED + p.getName() + "'s" + ChatColor.GREEN + " party.");
                                Bukkit.getPlayer(playerName).sendMessage(header +"Type /party accept " + ChatColor.RED + p.getName() + ChatColor.GREEN + " to join");
                                party.addInvite(playerName);
                                return true;
                            }
                        }
                    }
                }
                else
                {
                    if(args.length==0)
                    {

                        p.sendMessage( header+ "--------------------");
                        p.sendMessage(header + "party getowner");
                        p.sendMessage(header + "party leave");
                        p.sendMessage(header + "--------------------");
                        return true;
                    }
                    else
                    {
                        if(args[0].equalsIgnoreCase("getowner"))
                        {
                            p.sendMessage(PartyManager.getParty(p.getName()).getOwner() + " is the owner of this party");
                            return true;
                        }
                        else if(args[0].equalsIgnoreCase("leave"))
                        {
                            for(String mem : PartyManager.getParty(p.getName()).getMembers())
                            {
                                Player m= Bukkit.getPlayer(mem);
                                m.sendMessage(header + p.getName() + " has left the party");

                                //TODO remove player from party on disconnect
                            }
                            Bukkit.getPlayer(PartyManager.getParty(p.getName()).getOwner()).sendMessage(header + p.getName() + " has left the party");
                            PartyManager.getParty(p.getName()).removeMember(p.getName());
                            p.sendMessage(header + "You left the party");
                            return true;
                        }
                        else
                        {
                            sender.sendMessage(header + "Unknown command");
                            return true;
                        }
                    }
                }



            }
            else
            {
                if(args.length==0)
                {
                    p.sendMessage(header + "----------------");
                    p.sendMessage(header + "party invite [playername]");
                    p.sendMessage(header + "party accept [playername]");
                    p.sendMessage(header + "----------------");
                    return true;
                }
                if(args[0].equalsIgnoreCase("accept"))
                {
                    Party party = null;
                    for(Party pp : parties)
                    {
                        if(pp.getOwner().equalsIgnoreCase(args[1]))
                        {
                            party = pp;
                        }
                    }

                    if(party.getInvites().contains(sender.getName()))
                    {
                        party.removeInvite(sender.getName());
                        party.addMember(sender.getName());
                        sender.sendMessage(header + "You joined the party");
                        Bukkit.getPlayer(party.getOwner()).sendMessage(header + sender.getName() + " joined your party");
                        return true;
                    }
                    sender.sendMessage(header + "You do not have an invitation to this party");
                    return true;
                }
                if(args[0].equalsIgnoreCase("invite"))
                {
                    Player i = Bukkit.getPlayer(args[1]);
                    if(i==null)
                    {
                        p.sendMessage(header + "That player is not online!");
                        return true;
                    }
                    Party party = new Party(p.getName(),i.getName());
                    parties.add(party);
                    p.sendMessage(header + "Invitation sent!");
                    i.sendMessage(header + "You were invited to " + ChatColor.RED + p.getName() + "'s" + ChatColor.GREEN + " party.");
                    i.sendMessage(header +"Type /party accept " + ChatColor.RED + p.getName() + ChatColor.GREEN + " to join");
                    //   party.addInvite(playerName);
                    p.sendMessage(header + "Party created");
                    return true;

                }


            }
        }
        if(cmd.getName().equalsIgnoreCase("leave"))
        {
            if(storage.getArenaManager().isPlayerInArena(((Player) sender)))
            {
                Arena arena = storage.getArenaManager().getPlayerArena(((Player)sender));
                if(arena!=null)
                {
                    arena.removePlayer(sender.getName());
                }
                else
                {
                    sender.sendMessage(storage.getHeader() + "Oh noes! Something has gone wrong!");
                }
            }
            return true;
        }
        if(cmd.getName().equalsIgnoreCase("snow"))
        {
            List<String> commands = new ArrayList<String>();
            commands.add("snow kits (Displays the kits)");
            commands.add("snow info [kitname] (Displays info on a specific kit)");
            commands.add("snow purchased (Displays your purchased kits)");
            commands.add("snow gems (Displays the amount of gems you have)");
            commands.add("snow buy [kitname] (Buys a kit)");
            if(args.length==0 || args.length == 1 & args[0].equalsIgnoreCase("help"))
            {
                sender.sendMessage(storage.getHeader() + "Snow Game HELP");
                for(String s : commands)
                {
                    sender.sendMessage(storage.getHeader() + s);
                }
                return true;
            }

//            if(args[0].equalsIgnoreCase("buy") && args.length==2)
//            {
//                if(args[1].equalsIgnoreCase("archer"))
//                {
//                    int price = Archer.getPrice();
//                    if(storage.getSqlapi().getGems(sender.getName()) - price>=0)
//                    {
//                        if(storage.getSqlapi().getPurchases(sender.getName()).contains(args[1].toUpperCase()))
//                        {
//                            sender.sendMessage(header + "You already own this!");
//                            return true;
//                        }
//
//                        storage.getSqlapi().addPurchase(args[1].toUpperCase(), sender.getName());
//                        storage.getSqlapi().setGems(sender.getName(), storage.getSqlapi().getGems(sender.getName()) - price);
//
//                        sender.sendMessage(header + "You purchased " + args[1].toUpperCase());
//
//                        return true;
//                        //String purchases = storage.getSqlapi().getPurchases(sender.getName());
//
//
//                    }
//
//                    return true;
//                }
//
//                else if(args[1].equalsIgnoreCase("axeman"))
//                {
//                    int price = Axeman.getPrice();
//                    if(storage.getSqlapi().getGems(sender.getName()) - price>=0)
//                    {
//                        if(storage.getSqlapi().getPurchases(sender.getName()).contains(args[1].toUpperCase()))
//                        {
//                            sender.sendMessage(header + "You already own this!");
//                            return true;
//                        }
//
//                        storage.getSqlapi().addPurchase(args[1].toUpperCase(), sender.getName());
//                        storage.getSqlapi().setGems(sender.getName(), storage.getSqlapi().getGems(sender.getName()) - price);
//
//                        sender.sendMessage(header + "You purchased " + args[1].toUpperCase());
//
//                        return true;
//                        //String purchases = storage.getSqlapi().getPurchases(sender.getName());
//
//
//                    }
//
//                    return true;
//                }
//                else if(args[1].equalsIgnoreCase("butcher"))
//                {
//                    int price = Butcher.getPrice();
//                    if(storage.getSqlapi().getGems(sender.getName()) - price>=0)
//                    {
//                        if(storage.getSqlapi().getPurchases(sender.getName()).contains(args[1].toUpperCase()))
//                        {
//                            sender.sendMessage(header + "You already own this!");
//                            return true;
//                        }
//
//                        storage.getSqlapi().addPurchase(args[1].toUpperCase(), sender.getName());
//                        storage.getSqlapi().setGems(sender.getName(), storage.getSqlapi().getGems(sender.getName()) - price);
//
//                        sender.sendMessage(header + "You purchased " + args[1].toUpperCase());
//
//                        return true;
//                        //String purchases = storage.getSqlapi().getPurchases(sender.getName());
//
//
//                    }
//
//                    return true;
//                }
//                else if(args[1].equalsIgnoreCase("Ghost"))
//                {
//                    int price = Ghost.getPrice();
//                    if(storage.getSqlapi().getGems(sender.getName()) - price>=0)
//                    {
//                        if(storage.getSqlapi().getPurchases(sender.getName()).contains(args[1].toUpperCase()))
//                        {
//                            sender.sendMessage(header + "You already own this!");
//                            return true;
//                        }
//
//                        storage.getSqlapi().addPurchase(args[1].toUpperCase(), sender.getName());
//                        storage.getSqlapi().setGems(sender.getName(), storage.getSqlapi().getGems(sender.getName()) - price);
//
//                        sender.sendMessage(header + "You purchased " + args[1].toUpperCase());
//
//                        return true;
//                        //String purchases = storage.getSqlapi().getPurchases(sender.getName());
//
//
//                    }
//
//                    return true;
//                }
//                else
//                {
//                    sender.sendMessage(header + "Unknown kit! Try /snow kits");
//                    return true;
//                }
//            }
//
//            if(args[0].equalsIgnoreCase("gems"))
//            {
//                sender.sendMessage(header + storage.getSqlapi().getGems(sender.getName()));
//                return true;
//            }
//
//
//            if(args[0].equalsIgnoreCase("purchased"))
//            {
//                sender.sendMessage(header + "Purchased Kits");
//                String s = storage.getSqlapi().getPurchases(sender.getName());
//                String[] split = s.split(", ");
//                for(String ss : split)
//                {
//                    sender.sendMessage(header + ss);
//                }
//                return true;
//            }
//            else if(args[0].equalsIgnoreCase("kits"))
//            {
//                sender.sendMessage(header + "Archer - " + Archer.getPrice() + " Gems");
//                sender.sendMessage(header + "Axeman - " + Axeman.getPrice() + " Gems");
//                sender.sendMessage(header + "Butcher - " + Butcher.getPrice() + " Gems");
//                sender.sendMessage(header + "Ghost - " + Ghost.getPrice() + " Gems");
//                return true;
//            }
//            else if(args[0].equalsIgnoreCase("info") && args.length==2)
//            {
//                if(args[1].equalsIgnoreCase("archer"))
//                {
//                    sender.sendMessage(header + "Archer Information:");
//                    sender.sendMessage(header + "Bow (1): Arrow Infinity, Arrow Flame");
//                    sender.sendMessage(header + "Arrow (5)");
//                    sender.sendMessage(header + "Potion Effect: Speed 2");
//                    sender.sendMessage(header + "Gems: " + Archer.getPrice());
//                    return true;
//                }
//                else if(args[1].equalsIgnoreCase("axeman"))
//                {
//                    sender.sendMessage(header + "Axeman Information:");
//                    sender.sendMessage(header + "Stone Axe (1): Sharpness (2), Flame");
//                    sender.sendMessage(header + "Cooked Chicken (5)");
//                    sender.sendMessage(header + "Potion Effect: Slowness 2");
//                    sender.sendMessage(header + "Gems: " + Axeman.getPrice());
//                    return true;
//                }
//                else if(args[1].equalsIgnoreCase("butcher"))
//                {
//                    sender.sendMessage(header + "Butcher Information:");
//                    sender.sendMessage(header + "Wood Sword (3)");
//                    sender.sendMessage(header + "Cooked Fish (5)");
//                    sender.sendMessage(header + "Potion Effect: Jump 2");
//                    sender.sendMessage(header + "Gems: " + Butcher.getPrice());
//                    return true;
//                }
//                else if(args[1].equalsIgnoreCase("Ghost"))
//                {
//                    sender.sendMessage(header + "Ghost Information:");
//                    sender.sendMessage(header + "Stick (1): Sharpness (4)");
//                    sender.sendMessage(header + "Potion Effect: Invisibility (1)");
//                    sender.sendMessage(header + "Gems: " + Ghost.getPrice());
//                    return true;
//                }
//                else
//                {
//                    sender.sendMessage(header + "Unknown kit! Try /snow kits");
//                    return true;
//                }
//            }


        }

        if (cmd.getName().equalsIgnoreCase("tr")) { // If the player typed /basic then do the following...
            if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
                List<String> commands = new ArrayList<String>();
                commands.add(cmd.getName() + " creategame [GAME]");
                commands.add(cmd.getName() + " setlobby [GAME] (stand on new lobby)");
                commands.add(cmd.getName() + " setplayerspawn [GAME] [1/maxplayers] (stand on players spawn)");
                commands.add(cmd.getName() + " setworldspawn (stand on new spawn)");
                commands.add(cmd.getName() + " setcorner [GAME] [1/2] (look at corner)");
                commands.add(cmd.getName() + " setsign [GAME] (look at sign)");
                commands.add(cmd.getName() + " setcenter [GAME] (stand on center)");
                commands.add(cmd.getName() + " setmaxplayers [#]");
                commands.add(cmd.getName() + " updatesigns (Updates signs)");
                commands.add(cmd.getName() + " updatearena [GAME] (Updates arena *after making any changes to arena this is required*)");
                commands.add(cmd.getName() + " setrank [playername] [rank]");
                commands.add(cmd.getName() + " setgems [playername] [gems]");
                commands.add(cmd.getName() + " generatetables");
                commands.add(cmd.getName() + " settopy [arenaname]");
                commands.add(cmd.getName() + " setportal [arenaname]");
                //  commands.add(cmd.getName() + " editmode (removes wither that spawns on top of you)");
                for (String s : commands) {
                    sender.sendMessage(storage.getHeader() + s);
                }
                return true;
            } else {
                if(args[0].equalsIgnoreCase("setportal"))
                {
                    Location loc = ((Player)sender).getLocation();
                    int x = loc.getBlockX();
                    int y = loc.getBlockY();
                    int z = loc.getBlockZ();
                    String name = args[1].toUpperCase();
                    getConfig().set(name + ".Portal.X", x);
                    getConfig().set(name + ".Portal.Y", y);
                    getConfig().set(name + ".Portal.Z", z);
                    saveConfig();
                    reloadConfig();
                    return true;

                }




                if(args[0].equalsIgnoreCase("generatetables"))
                {
                    try
                    {
                        storage.getSqlapi().generateTable();
                        sender.sendMessage(storage.getHeader() + "We're all good.");
                    }catch (Exception ex){
                        sender.sendMessage(storage.getHeader() + "Oh noes! Something went wrong!");
                    }
                    return true;

                }

                if(args[0].equalsIgnoreCase("setgems"))
                {
                    Player p = Bukkit.getPlayer(args[1]);
                    if(p!=null)
                    {
                        storage.getSqlapi().setGems(p.getName(),Integer.parseInt( args[2]));
                    }
                    else
                    {
                        storage.getSqlapi().setGems(args[1],Integer.parseInt( args[2]));
                    }
                    sender.sendMessage(storage.getHeader() + "Set gems: " + args[2]);
                    return true;
                }
                if(args[0].equalsIgnoreCase("setrank"))
                {
                    Player p = Bukkit.getPlayer(args[1]);
                    if(p!=null)
                    {
                        storage.getSqlapi().setRank(p.getName(), args[2]);
                    }
                    else
                    {
                        storage.getSqlapi().setRank(args[1], args[2]);
                    }
                    sender.sendMessage(storage.getHeader() + "Set rank: " + args[2]);
                    return true;
                }


                if(args[0].equalsIgnoreCase("updatearena"))
                {
                    final Arena a = storage.getArenaManager().getArenaByName(args[1].toUpperCase());
                    System.out.print(storage.getArenas());
                    Bukkit.broadcastMessage(a.getName());
                    if(a!=null)
                    {
                        if(a.getState()== ArenaState.OFF)
                        {
                            storage.removeArena(a);
                            storage.addArena(createArena(a.getName()));
                        }
                        else if(a.getState()==ArenaState.ON)
                        {
                            a.setTime(1);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                                @Override
                                public void run() {
                                    storage.removeArena(a);
                                    storage.addArena(createArena(a.getName()));
                                }
                            }, 40);
                        }
                        else
                        {
                            a.endGame();
                            storage.removeArena(a);
                            storage.addArena(createArena(a.getName()));
                        }
                        sender.sendMessage(storage.getHeader() + "Updated");
                        return true;
                    }
                    createArena(args[1].toUpperCase());
                    sender.sendMessage(storage.getHeader() + "Created");
                    return true;


                }
                if (args[0].equalsIgnoreCase("updatesigns")) {
                    sender.sendMessage(storage.getHeader() + "Updating signs...");
                    for(Arena arenaAPI: storage.getArenas())
                    {
                        storage.getSignManager().updateSign(arenaAPI);
                    }
                    sender.sendMessage(storage.getHeader() + "Update complete!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("setmaxplayers")) {
                    int i = Integer.parseInt(args[1]);
                    storage.setMaxPlayers(i);
                    getConfig().set("MaxPlayers", i);
                    saveConfig();
                    reloadConfig();




                    sender.sendMessage(storage.getHeader() + "Set max players to " + ChatColor.RED + storage.getMain());
                    return true;
                }

                if (args[0].equalsIgnoreCase("setcenter")) {
                    Player p = sender instanceof Player ? ((Player) sender) : null;
                    if (p == null) {
                        sender.sendMessage(storage.getHeader() + "YOU NNEEEEDDD TO BE A PLAYER!!");
                        return true;
                    } else {
                        int x = p.getLocation().getBlockX();
                        int y = p.getLocation().getBlockY();
                        int z = p.getLocation().getBlockZ();

                        getConfig().set(args[1].toUpperCase() + ".Center.X", x);
                        getConfig().set(args[1].toUpperCase() + ".Center.Y", y);
                        getConfig().set(args[1].toUpperCase() + ".Center.Z", z);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Set " + args[1].toUpperCase() + "\'s center to your current location!");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("setsign")) {
                    String game = args[1].toUpperCase();
                    Player p = sender instanceof Player ? ((Player) sender) : null;
                    if (p == null) {
                        sender.sendMessage(storage.getHeader() + "You need to be a player!");
                    } else {
                        Block b = p.getTargetBlock(null, 20);
                        int x = b.getLocation().getBlockX();
                        int y = b.getLocation().getBlockY();
                        int z = b.getLocation().getBlockZ();
                        String configLocation = game + ".Sign.";
                        getConfig().set(configLocation + "X", x);
                        getConfig().set(configLocation + "Y", y);
                        getConfig().set(configLocation + "Z", z);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Set the sign\'s location to your currently targeted block's location");

                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("setcorner")) {
                    String game = args[1].toUpperCase();
                    int corner = Integer.parseInt(args[2]);
                    if (corner > 2) {
                        sender.sendMessage(storage.getHeader() + "How many corners are you supposed to have?? (2)");
                        return true;
                    } else {
                        Block b = ((Player) sender).getTargetBlock(null, 20);
                        int x = b.getLocation().getBlockX();
                        int y = b.getLocation().getBlockY();
                        int z = b.getLocation().getBlockZ();
                        String configLocation = game + ".Corner" + corner + ".";
                        getConfig().set(configLocation + "X", x);
                        getConfig().set(configLocation + "Y", y);
                        getConfig().set(configLocation + "Z", z);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Set corner " + ChatColor.RED + corner + " to your currently targeted block's location");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("setworldspawn")) {
                    Player p = sender instanceof Player ? ((Player) sender) : null;
                    if (p == null) {
                        sender.sendMessage(storage.getHeader() + "You need to be a player to send this command!");
                        return true;
                    } else {
                        int x = p.getLocation().getBlockX();
                        int y = p.getLocation().getBlockY();
                        int z = p.getLocation().getBlockZ();
                        p.getWorld().setSpawnLocation(x, y, z);

                        sender.sendMessage(storage.getHeader() + "Set the world\'s spawn location to your current location!");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("setplayerspawn")) {
                    String game = args[1].toUpperCase();
                    int playerNum = Integer.parseInt(args[2]);
                    String location = game + ".Player" + playerNum + ".";
                    Player p = sender instanceof Player ? ((Player) sender) : null;
                    if (p == null) {
                        sender.sendMessage(storage.getHeader() + "You need to be a player to send this command!");
                        return true;
                    } else {
                        int x = p.getLocation().getBlockX();
                        int y = p.getLocation().getBlockY();
                        int z = p.getLocation().getBlockZ();

                        getConfig().set(location + "X", x);
                        getConfig().set(location + "Y", y);
                        getConfig().set(location + "Z", z);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Set Player " + playerNum + "\'s spawn point to your current location!");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("creategame")) {
                    if (!getConfig().isSet(args[1].toUpperCase())) {
                        getConfig().set(args[1].toUpperCase() + ".Created", true);


                        games.add(args[1].toUpperCase());

                        getConfig().set("Games", games);


                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Created the game " + ChatColor.RED + args[1].toUpperCase());
                        return true;
                    } else {
                        sender.sendMessage(storage.getHeader() + "Sorry that game exists");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("setlobby")) {
                    Player p = sender instanceof Player ? ((Player) sender) : null;
                    if (p == null) {
                        sender.sendMessage(storage.getHeader() + "You need to be a player to send this command!");
                        return true;
                    } else {
                        int x = p.getLocation().getBlockX();
                        int y = p.getLocation().getBlockY();
                        int z = p.getLocation().getBlockZ();

                        getConfig().set(args[1].toUpperCase() + ".Lobby.X", x);
                        getConfig().set(args[1].toUpperCase() + ".Lobby.Y", y);
                        getConfig().set(args[1].toUpperCase() + ".Lobby.Z", z);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(storage.getHeader() + "Set " + args[1].toUpperCase() + "\'s lobby  to your current location!");
                        return true;
                    }
                }
                if(args[0].equalsIgnoreCase("seturl"))
                 {
                        getConfig().set("URL", args[1]);
                        saveConfig();
                        reloadConfig();
                        return true;
                }
                if(args[0].equalsIgnoreCase("setport"))
                {
                    getConfig().set("Port", Integer.parseInt(args[1]));
                    saveConfig();
                    reloadConfig();
                    return true;
                }
                if(args[0].equalsIgnoreCase("setusername"))
                {
                    getConfig().set("Username", args[1]);
                    saveConfig();
                    reloadConfig();
                    return true;
                }
                if(args[0].equalsIgnoreCase("setpassword"))
                {
                    getConfig().set("Password",args[1]);
                    saveConfig();
                    reloadConfig();
                    return true;
                }
                if(args[0].equalsIgnoreCase("setdatabase"))
                {
                    getConfig().set("Database",args[1]);
                    saveConfig();
                    reloadConfig();
                    return true;
                }
            }
            return true;
        } //If this has happened the function will return true.
        // If this hasn't happened the value of false will be returned.
        return false;
    }
}
