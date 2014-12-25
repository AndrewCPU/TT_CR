package monkeyboystein.utils;

import monkeyboystein.Arena.Arena;
import monkeyboystein.Arena.Team;
import monkeyboystein.Main;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class EventListener implements Listener {
    Storage storage = Main.storage;
    int RED = 14;
    int BLACK = 15;
    int WHITE = 0;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (storage.getArenaManager().isPlayerInArena(event.getPlayer())) {
            Team team = storage.getArenaManager().getPlayerTeam(event.getPlayer());
            if (team != null) {
                int color = 0;
                if (team.getName().equalsIgnoreCase("RED")) {
                    color = 14;
                }
                if (team.getName().equalsIgnoreCase("BLACK")) {
                    color = 15;
                }
                if (team.getName().equalsIgnoreCase("WHITE")) {
                    color = 0;
                }
                HashMap<String, Integer> points = determinePointsFromTeam(event.getPlayer(), color);
                Arena arena = storage.getArenaManager().getPlayerArena(event.getPlayer());

                arena.getBlack().setScore(arena.getBlack().getScore() - points.get("BLACK"));
                arena.getRed().setScore(arena.getRed().getScore() - points.get("RED"));
                arena.getWhite().setScore(arena.getWhite().getScore() - points.get("WHITE"));

                for (Location loc : getBlocks(event.getPlayer(), color)) {
                    boolean contains = false;
                    for (Location cbl : arena.changedBlock) {
                        if (cbl == loc) {
                            contains = true;
                        }

                    }
                    if (!contains) {
                        arena.changedBlock.add(loc);
                    }
                    loc.getBlock().setData((byte) color);
                    team.setScore(team.getScore()+1);

                }

            }

            if(event.getPlayer().getLocation().getBlockY()<=15)
            {
                Arena a = storage.getArenaManager().getPlayerArena(event.getPlayer());

                Location spawn = a.getArenaSpawnManager().getSpawn(team.getName());

                event.getPlayer().setVelocity(new Vector(0,0,0));
                event.getPlayer().teleport(spawn);

                for(int i = 0; i<=50; i++)
                {
                    int rand = new Random().nextInt(a.changedBlock.size() + 1 - 1) + 1;
                    Location loc = a.changedBlock.get(rand);
                    loc.getBlock().setData((byte)8);
                    a.changedBlock.remove(rand);
                    if(team.getScore() - 50 >=0 )
                    {
                        team.setScore(team.getScore() - 50);
                    }
                }
              //  Team team = storage.getArenaManager().getPlayerTeam()
            }


        }
    }

    public HashMap<String, Integer> determinePointsFromTeam(Player p, int COLOR) {
        HashMap<String, Integer> points = new HashMap<String, Integer>();
        points.put("RED", 0);
        points.put("WHITE", 0);
        points.put("BLACK", 0);
        Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (b.getData() != COLOR) {
            // blocks.add(b.getLocation());
            if (b.getData() == RED) {
                int r = points.get("RED");
                r++;
                points.remove("RED");
                points.put("RED", r);
            } else if (b.getData() == BLACK) {
                int r = points.get("BLACK");
                r++;
                points.remove("BLACK");
                points.put("BLACK", r);
            } else if (b.getData() == WHITE) {
                int r = points.get("WHITE");
                r++;
                points.remove("WHITE");
                points.put("WHITE", r);
            }

        }
        Location orig = b.getLocation();
        b = b.getLocation().add(1, 0, 0).getBlock();
        if (b.getData() != COLOR) {
            //   blocks.add(b.getLocation());
            if (b.getData() == RED) {
                int r = points.get("RED");
                r++;
                points.remove("RED");
                points.put("RED", r);
            } else if (b.getData() == BLACK) {
                int r = points.get("BLACK");
                r++;
                points.remove("BLACK");
                points.put("BLACK", r);
            } else if (b.getData() == WHITE) {
                int r = points.get("WHITE");
                r++;
                points.remove("WHITE");
                points.put("WHITE", r);
            }
        }
        b = orig.getBlock();
        b = b.getLocation().add(-1, 0, 0).getBlock();
        if (b.getData() != COLOR) {
            //   blocks.add(b.getLocation());
            if (b.getData() == RED) {
                int r = points.get("RED");
                r++;
                points.remove("RED");
                points.put("RED", r);
            } else if (b.getData() == BLACK) {
                int r = points.get("BLACK");
                r++;
                points.remove("BLACK");
                points.put("BLACK", r);
            } else if (b.getData() == WHITE) {
                int r = points.get("WHITE");
                r++;
                points.remove("WHITE");
                points.put("WHITE", r);
            }
        }
        b = orig.getBlock();
        b = b.getLocation().add(0, 0, 1).getBlock();
        if (b.getData() != COLOR) {
            /// blocks.add(b.getLocation());
            if (b.getData() == RED) {
                int r = points.get("RED");
                r++;
                points.remove("RED");
                points.put("RED", r);
            } else if (b.getData() == BLACK) {
                int r = points.get("BLACK");
                r++;
                points.remove("BLACK");
                points.put("BLACK", r);
            } else if (b.getData() == WHITE) {
                int r = points.get("WHITE");
                r++;
                points.remove("WHITE");
                points.put("WHITE", r);
            }
        }
        b = orig.getBlock();
        b = b.getLocation().add(0, 0, -1).getBlock();
        if (b.getData() != COLOR) {
            //blocks.add(b.getLocation());
            if (b.getData() == RED) {
                int r = points.get("RED");
                r++;
                points.remove("RED");
                points.put("RED", r);
            } else if (b.getData() == BLACK) {
                int r = points.get("BLACK");
                r++;
                points.remove("BLACK");
                points.put("BLACK", r);
            } else if (b.getData() == WHITE) {
                int r = points.get("WHITE");
                r++;
                points.remove("WHITE");
                points.put("WHITE", r);
            }
        }
        return points;
    }

    public List<Location> getBlocks(Player p, int COLOR)
    {
        List<Location> blocks = new ArrayList<Location>();
        Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(b.getData()!=COLOR)
        {
            blocks.add(b.getLocation());
        }
        Location orig = b.getLocation();
        b = b.getLocation().add(1,0,0).getBlock();
        if(b.getData()!=COLOR)
        {
            blocks.add(b.getLocation());
        }
        b = orig.getBlock();
        b = b.getLocation().add(-1,0,0).getBlock();
        if(b.getData()!=COLOR)
        {
            blocks.add(b.getLocation());
        }
        b = orig.getBlock();
        b = b.getLocation().add(0,0,1).getBlock();
        if(b.getData()!=COLOR)
        {
            blocks.add(b.getLocation());
        }
        b = orig.getBlock();
        b = b.getLocation().add(0,0,-1).getBlock();
        if(b.getData()!=COLOR)
        {
            blocks.add(b.getLocation());
        }
        return blocks;

    }
}
