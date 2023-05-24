package back;

import common.GameMap;
import common.Player;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private final int MAX_PLAYERS = 4;
    public GameMap gamemap;
    ArrayList<Player> playersList;

    public Game() {
        this.playersList = new ArrayList<>(4);
        this.gamemap = new GameMap();

        // Test safezones
        gamemap.insertSafeZone(30, 5, 5, 5);
        gamemap.insertSafeZone(90, 90, 5, 5);
    }

    public boolean addPlayer(String nickname, ObjectOutputStream objectOutputStream) {
        if (playersList.size() >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return false;

        int id = getFirstFreeID();

        Player player = new Player(nickname, id, objectOutputStream);
        playersList.add(player);

        return true;
    }

    private int getFirstFreeID() {

        if (playersList.size() == 0)
            return 0;

        for (int i = 0; i < MAX_PLAYERS; i++) {

            int busyFlag = 0;

            for (Player player : playersList) {
                if (i == player.getID()) {
                    busyFlag = 1;
                    break;
                }
            }

            if (busyFlag == 0)
                return i;

        }

        return 9;
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
