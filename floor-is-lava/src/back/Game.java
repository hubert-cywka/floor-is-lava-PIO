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

import static common.GlobalSettings.*;

public class Game implements Serializable {

    public GameMap gameMap;
    public ArrayList<Player> playersList;
    private final Timer timer;
    private int round;
    private boolean isWaitingForPlayers;

    public Game() {
        this.playersList = new ArrayList<>();
        this.gameMap = new GameMap(this);
        timer = new Timer();
        this.isWaitingForPlayers = true;
        this.round = 0;
    }

    public boolean isWaitingForPlayers() {
        return this.isWaitingForPlayers;
    }

    public void setWaitingForPlayers(boolean isWaiting) {
        this.isWaitingForPlayers = isWaiting;
    }

    public int getRound() {
        return this.round;
    }

    public void incrementRound() {
        this.round += 1;
    }

    public void resetRound() {
        this.round = 0;
    }

    public void startGame() {
        setWaitingForPlayers(false);
    }

    public void restartGame() {
        setWaitingForPlayers(true);
        this.gameMap.generateMap();
        this.getTimer().setTimer(ROUND_TIME);
        this.resetRound();

        playersList.forEach(player -> player.setGamesWon(0));
    }

    public Player addPlayer(String nickname, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        if (playersList.size() >= MAX_PLAYERS || hasSomeoneTheSameNickname(nickname))
            return null;

        int id = getFirstFreeID();

        Player player = new Player(nickname, id, objectOutputStream, objectInputStream, isWaitingForPlayers);
        playersList.add(player);
        insertPlayerToMap(player);
        if (!player.isAlive()) killPlayer(player);

        if (playersList.size() >= MIN_PLAYERS) {
            this.startGame();
        }

        return player;
    }

    public void addPowerUpOnMap(PowerUp power) {
        Position powerpos = findValidPositionOnMap();
        power.setPosition(powerpos);
        gameMap.insertPowerUp(power);
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public boolean isThatField(Position pos, FieldType fieldType) {
        try {
            return gameMap.getMap()[pos.row][pos.col] == fieldType;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isPositionValid(Position pos) {
        try {
            FieldType[][] map = gameMap.getMap();
            return map[pos.row][pos.col] == FieldType.FLOOR || map[pos.row][pos.col] == FieldType.SAFE_ZONE;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Position findValidPositionOnMap() {

        Position newPosition = new Position(getRandomNumberInRange(0, gameMap.getHeight()), getRandomNumberInRange(0, gameMap.getWidth()));

        while (!isThatField(newPosition, FieldType.SAFE_ZONE)) {
            newPosition.row = getRandomNumberInRange(0, gameMap.getHeight());
            newPosition.col = getRandomNumberInRange(0, gameMap.getWidth());
        }

        return newPosition;

    }

    public void insertPlayerToMap(Player p) {
        Position playerpos = findValidPositionOnMap();
        p.setPosition(playerpos);
        p.setLastStandingField(FieldType.SAFE_ZONE);
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
        player.setConnected(false);
        gameMap.removePlayerFromMap(player);
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

    public Player findPlayerByNickname(String nickname) {
        for (Player player : playersList) {
            if (player.getNickname().equalsIgnoreCase(nickname))
                return player;
        }
        return null;
    }

    public Player findPlayerById(int id) {

        for (Player player : playersList) {
            if (player.getID() == id)
                return player;
        }

        return null;

    }

    public Player findPlayerByFiledType(FieldType fieldType) {

        if (!PLAYER_FIELDS.contains(fieldType))
            return null;

        String textField = fieldType.toString();

        try{
            for (int i = 0; i < MAX_PLAYERS; i++) {
                if (textField.contains(String.valueOf(i)))
                    return playersList.get(i);
            }
        }catch (IndexOutOfBoundsException e){
            return null;
        }

        return null;
    }

    public void killPlayer(Player player) {
        player.setAlive(false);
        gameMap.removePlayerFromMap(player);
    }


    public void movePlayer(Player player, Direction move) {
        if(player.getRoundsBoostedSpeed() > 0) {
            gameMap.movePlayer(player, move);
            gameMap.movePlayer(player, move);
        }else{
            gameMap.movePlayer(player, move);
        }
    }

    public Timer getTimer() {
        return timer;
    }
}
