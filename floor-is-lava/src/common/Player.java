package common;

import back.Position;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Player implements Serializable {
    private final String nickname;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public Position position;
    private boolean isAlive;
    private boolean isMoving;
    private final int ID;
    private static PlayerMove nextPlayerMove = new PlayerMove(Direction.NO_MOVE, Direction.NO_MOVE);

    public Player(String nickname, int ID, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.nickname = nickname;
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;
        this.ID = ID;
        this.isAlive = true;
        position = new Position(-1, -1);
    }

    public static PlayerMove getNextPlayerMove() {
        return nextPlayerMove;
    }

    public static void clearNextPlayerMove() {
        nextPlayerMove.setHorizontal(Direction.NO_MOVE);
        nextPlayerMove.setVertical(Direction.NO_MOVE);
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
}
