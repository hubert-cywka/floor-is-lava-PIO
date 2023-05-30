package common;

import java.io.Serializable;

public class PlayerData implements Serializable {

    private String nickname;
    private boolean isAlive;
    private boolean isConnected;
    private int ID;

    public PlayerData(String nickname, boolean isAlive, boolean isConnected, int ID) {
        this.nickname = nickname;
        this.isAlive = isAlive;
        this.isConnected = isConnected;
        this.ID = ID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
