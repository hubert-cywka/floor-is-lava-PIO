package common;

import back.Position;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Player implements Serializable {
    private final String nickname;
    private final ObjectOutputStream objectOutputStream;
    private final ObjectInputStream objectInputStream;
    public Position position;
    private static boolean isAlive;
    private FieldType lastStandingField;
    private static boolean isConnected;
    private final int ID;
    private int gamesWon;
    private static final PlayerMove nextPlayerMove = new PlayerMove(Direction.NO_MOVE, Direction.NO_MOVE);
    private int roundsBoostedGhost;
    private int roundsBoostedSpeed;

    public Player(String nickname, int ID, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, boolean isAlive) {
        this.nickname = nickname;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        this.isConnected = true;
        this.ID = ID;
        this.isAlive = isAlive;
        this.roundsBoostedGhost = 0;
        this.roundsBoostedSpeed = 0;
        this.lastStandingField = FieldType.SAFE_ZONE;
        this.gamesWon = 0;
        position = new Position(-1, -1);
    }


    public FieldType getLastStandingField() {
        return lastStandingField;
    }

    public void setLastStandingField(FieldType lastStandingField) {
        this.lastStandingField = lastStandingField;
    }

    public static PlayerMove getNextPlayerMove() {
        return nextPlayerMove;
    }

    public static void clearNextPlayerMove() {
        nextPlayerMove.setHorizontal(Direction.NO_MOVE);
        nextPlayerMove.setVertical(Direction.NO_MOVE);
    }

    public void decrementPowerUpRound() {
        if (getRoundsBoostedGhost() > 0)
            roundsBoostedGhost--;
        if (getRoundsBoostedSpeed() > 0)
            roundsBoostedSpeed--;
    }

    public int getID() {
        return ID;
    }

    public static void setAlive(boolean alive) {
        isAlive = alive;
    }

    public static boolean isAlive() {
        return isAlive;
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public static void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public String getNickname() {
        return nickname;
    }

    public ObjectOutputStream getOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getInputStream() {
        return objectInputStream;
    }

    public void setRoundsBoostedGhost(int rounds) {
        roundsBoostedGhost = rounds;
    }

    public void setRoundsBoostedSpeed(int rounds) {
        roundsBoostedSpeed = rounds;
    }

    public void addRoundsBoostedGhost(int rounds) {
        roundsBoostedGhost += rounds;
    }

    public void addRoundsBoostedSpeed(int rounds) {
        roundsBoostedSpeed += rounds;
    }

    public int getRoundsBoostedGhost() {
        return roundsBoostedGhost;
    }

    public int getRoundsBoostedSpeed() {
        return roundsBoostedSpeed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void incrementGamesWon() {
        this.gamesWon += 1;
    }
}
