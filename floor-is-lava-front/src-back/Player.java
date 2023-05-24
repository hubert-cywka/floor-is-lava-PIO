import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable {
    private String nickname;
    private ObjectOutputStream objectOutputStream;
    private boolean isConnected;
    private int posX;
    private int posY;
    private boolean isAlive;
    private boolean isMoving;

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Player(String nickname, ObjectOutputStream objectOutputStream){
        this.nickname = nickname;
        this.objectOutputStream = objectOutputStream;
    }

    public String getNickname() { return nickname; }

    public ObjectOutputStream getOutputStream() { return objectOutputStream; }


}
