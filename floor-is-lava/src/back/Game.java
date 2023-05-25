package back;

import common.GameMap;
import common.Player;
import common.PowerUp;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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

        // test powerups
        addPowerUpOnMap(new PowerUp('x', generateValidPositionOnMap()));
        addPowerUpOnMap(new PowerUp('y', generateValidPositionOnMap()));
        addPowerUpOnMap(new PowerUp('y', generateValidPositionOnMap()));
    }

    public boolean addPlayer(String nickname, ObjectOutputStream objectOutputStream) {
        if (playersList.size() >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return false;

        int id = getFirstFreeID();

        Player player = new Player(nickname, id, objectOutputStream);
        playersList.add(player);
        insertPlayerToMap(player);
        return true;
    }

    public void addPowerUpOnMap(PowerUp power)
    {
        Position powerpos = generateValidPositionOnMap();
        power.setPosition(powerpos);
        gamemap.insertPowerUp(power);
    }

    public int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
    public boolean validPositionOnMap(Position pos)
    {
        char[][] map = gamemap.getMap();
        return map[pos.x][pos.y] == '.';
    }
    public Position generateValidPositionOnMap() {
        Position playerpos = new Position(getRandomNumberInRange(0, gamemap.getHeight()), getRandomNumberInRange(0, gamemap.getWidth()));
        while (!validPositionOnMap(playerpos))
        {
            playerpos.x = getRandomNumberInRange(0, gamemap.getHeight());
            playerpos.y = getRandomNumberInRange(0, gamemap.getWidth());
        }
        return playerpos;
    }
    public void insertPlayerToMap(Player p)
    {
        Position playerpos = generateValidPositionOnMap();
        p.setPosition(playerpos);
        gamemap.insertPlayer(p);
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
