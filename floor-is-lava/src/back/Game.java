package back;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private final int MAX_PLAYERS = 4;

    ArrayList<Player> playersList;

    public Game() {
        this.playersList = new ArrayList<>(4);
    }

    public boolean addPlayer(String nickname, ObjectOutputStream objectOutputStream) {
        if (playersList.size() >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return false;

        Player player = new Player(nickname, objectOutputStream);
        playersList.add(player);

        return true;
    }

    public void removePlayer(String nickname) {
        if (playersList.size() == 0)
            return;

        Player player = findPlayerByNickname(nickname);
        if (player == null)
            return;

        playersList.remove(player);
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public int getNumberOfPlayers() {
        return playersList.size();
    }

    private boolean hasSomeoneTheSameNickname(String nickname) {
        for (Player player : playersList) {
            if (player.getNickname().equalsIgnoreCase(nickname))
                return true;
        }

        return false;
    }

    private Player findPlayerByNickname(String nickname) {
        for (Player player : playersList) {
            if (player.getNickname().equalsIgnoreCase(nickname))
                return player;
        }
        return null;
    }
}
