import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable {
    private String nickname;
    private Socket socket;
    private boolean isConnected;
    private int posX;
    private int posY;
    private boolean isAlive;
    private boolean isMoving;
    private int ID;

    public int getID() {
        return ID;
    }

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

    //NEED CHANGES LATER
    public Player(String nickname, Socket socket){
        this.nickname = nickname;
        this.socket = socket;
    }

    public String getNickname() { return nickname; }

    public Socket getSocket() { return socket; }


}
