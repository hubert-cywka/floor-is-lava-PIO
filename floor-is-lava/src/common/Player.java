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
    private boolean isAlive;
    private FieldType lastStandingField;
    private static boolean isConnected;
    private final int ID;
    private static final PlayerMove nextPlayerMove = new PlayerMove(Direction.NO_MOVE, Direction.NO_MOVE);
    private static int roundsBoostedGhost;
    private static int roundsBoostedSpeed;
    private static boolean isBoostedWithSpeed;
    private static boolean isBoostedWithGhost;


    public Player(String nickname, int ID, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.nickname = nickname;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        this.isConnected = true;
        this.ID = ID;
        this.isAlive = true;
        roundsBoostedGhost = 0;
        roundsBoostedSpeed = 0;
        isBoostedWithSpeed = false;
        isBoostedWithGhost = false;
        this.lastStandingField = FieldType.SAFE_ZONE;
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

    public static void decrementPowerUpRound(){
        if(roundsBoostedGhost>0)
            roundsBoostedGhost--;
        else
            isBoostedWithGhost = false;
        if(roundsBoostedSpeed>0)
            roundsBoostedSpeed--;
        else
            isBoostedWithSpeed = false;
    }

    public int getID() {
        return ID;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
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

    public void setRoundsBoostedGhost() { roundsBoostedGhost+=3; }

    public void setRoundsBoostedSpeed() { roundsBoostedSpeed+=3; }

    public int getRoundsBoostedGhost() { return roundsBoostedGhost; }

    public int getRoundsBoostedSpeed() { return roundsBoostedSpeed; }

    public boolean isBoostedWithSpeed(){return isBoostedWithSpeed;}

    public boolean isBoostedWithGhost(){return isBoostedWithGhost;}

    public void setBoostedWithSpeed(){isBoostedWithSpeed = true;}

    public void setBoostedWithGhost(){isBoostedWithGhost = true;}
}
