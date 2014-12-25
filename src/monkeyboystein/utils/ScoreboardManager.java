package monkeyboystein.utils;

import monkeyboystein.Arena.Arena;
import monkeyboystein.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class ScoreboardManager {
    Storage storage = Main.storage;
    public void updateScoreboard(Arena arena)
    {
        Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(arena.getName(), "dummy");
        objective.setDisplayName(ChatColor.BLUE + "Run the colors");
        Score black = objective.getScore(ChatColor.BLUE+"BLACK");
        Score white = objective.getScore(ChatColor.WHITE + "WHITE");
        Score red = objective.getScore(ChatColor.RED + "RED");
        black.setScore(arena.getBlack().getScore());
        white.setScore(arena.getWhite().getScore());
        red.setScore(arena.getRed().getScore());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(String s : arena.getAllPlayers())
        {
            Bukkit.getPlayer(s).setScoreboard(scoreboard);

        }
    }
    public void updateMain(Player p)
    {

        Scoreboard scoreboard = storage.getMain().getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(p.getName(),"dummy");
        Score gems = objective.getScore(ChatColor.GREEN + "Gems");
        //gems set to database connection
        gems.setScore(15);
        int gemAmount = storage.getSqlapi().getGems(p.getName());
        Score gemValue = objective.getScore(ChatColor.BOLD + "" + gemAmount);
        gemValue.setScore(14);
        Score blankSpot1 = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
        blankSpot1.setScore(13);
        Score rank = objective.getScore(ChatColor.BLUE + "Rank");
        rank.setScore(12);
        String rankString = storage.getSqlapi().getRank(p.getName());
        Score rankValue = objective.getScore(ChatColor.BOLD  +rankString);
        rankValue.setScore(11);
        Score blank2 = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&b"));
        blank2.setScore(10);
        Score website = objective.getScore(ChatColor.GOLD + "Website");
        website.setScore(9);
        String websiteString = storage.getMain().getConfig().getString("Website");

        Score websiteValue = objective.getScore(websiteString);
        websiteValue.setScore(8);
        Score closingLine = objective.getScore("------------");
        closingLine.setScore(7);
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', storage.getMain().getConfig().getString("ScoreboardHeader")));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        p.setScoreboard(scoreboard);



    }

}
