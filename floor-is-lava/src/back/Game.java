package back;

import common.Player;
import common.PowerUp;
import common.FieldType;
import common.Direction;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static common.GlobalSettings.MAX_PLAYERS;

public class Game implements Serializable {

    public GameMap gameMap;
    public ArrayList<Player> playersList;
    private Timer timer;



    public Game() {
        this.playersList = new ArrayList<>(MAX_PLAYERS);
        this.gameMap = new GameMap();
        this.timer = new Timer();

        // Test safezones
        gameMap.insertZone(30, 5, 5, 5, FieldType.SAFE_ZONE);
        gameMap.insertZone(90, 90, 5, 5, FieldType.SAFE_ZONE);

        // test powerups
        addPowerUpOnMap(new PowerUp(FieldType.BOOST_SPEED, generateValidPositionOnMap()));
        addPowerUpOnMap(new PowerUp(FieldType.BOOST_SPEED, generateValidPositionOnMap()));
        addPowerUpOnMap(new PowerUp(FieldType.BOOST_GHOST, generateValidPositionOnMap()));
    }



    public Player addPlayer(String nickname, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        if (playersList.size() >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return null;

        int id = getFirstFreeID();

        Player player = new Player(nickname, id, objectOutputStream, objectInputStream);
        playersList.add(player);
        insertPlayerToMap(player);
        return player;
    }

    public void addPowerUpOnMap(PowerUp power) {
        Position powerpos = generateValidPositionOnMap();
        power.setPosition(powerpos);
        gameMap.insertPowerUp(power);
    }

    public int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public boolean validPositionOnMap(Position pos) {
        try{
            FieldType[][] map = gameMap.getMap();
            return map[pos.row][pos.col] == FieldType.FLOOR;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    public Position generateValidPositionOnMap() {
        Position playerpos = new Position(getRandomNumberInRange(0, gameMap.getHeight()), getRandomNumberInRange(0, gameMap.getWidth()));
        while (!validPositionOnMap(playerpos)) {
            playerpos.col = getRandomNumberInRange(0, gameMap.getHeight());
            playerpos.row = getRandomNumberInRange(0, gameMap.getWidth());
        }
        return playerpos;
    }

    public void insertPlayerToMap(Player p) {
        Position playerpos = generateValidPositionOnMap();
        p.setPosition(playerpos);
        gameMap.insertPlayer(p);
    }

    private int getFirstFreeID() {
        if (playersList.isEmpty())
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
        if (playersList.isEmpty())
            return;

        Player player = findPlayerByNickname(nickname);
        if (player == null)
            return;

        player.setAlive(false);
        gameMap.removePlayer(player);
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

    public void movePlayer(Player player, Direction move){
        gameMap.movePlayer(player, move);
    }

    public Timer getTimer(){
        return timer;
    }

}
