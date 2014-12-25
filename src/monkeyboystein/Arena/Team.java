package monkeyboystein.Arena;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class Team {
    private  List<String> players = new ArrayList<String>();
    private  int maxPlayers = 3;
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    private String name = "";

    public Team(String name) {
        this.name = name;
    }

    public int getMaxPlayers() {

        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayer(String player)
    {
        if(!this.players.contains(player))
        {
            this.players.add(player);
        }
    }

    public void removePlayer(String player)
    {
        if(this.players.contains(player))
        {
            this.players.remove(player);
        }
    }
}
