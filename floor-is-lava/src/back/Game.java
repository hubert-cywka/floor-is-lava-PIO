package back;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Game implements Serializable {
    private final int MAX_PLAYERS = 4;

    private int numberOfPlayers;
    ArrayList<Player> playersList;

    public Game() {
        this.numberOfPlayers = 0;
        this.playersList = new ArrayList<>(4);
    }

    public boolean addPlayer(String nickname, ObjectOutputStream objectOutputStream) {
        if (numberOfPlayers >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return false;

        Player player = new Player(nickname, objectOutputStream);
        playersList.add(player);
        numberOfPlayers++;

        return true;
    }

    public void removePlayer(String nickname) {
        if (numberOfPlayers == 0)
            return;

        Player player = findPlayerByNickname(nickname);
        if (player == null)
            return;

        playersList.remove(player);
        numberOfPlayers--;
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
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
