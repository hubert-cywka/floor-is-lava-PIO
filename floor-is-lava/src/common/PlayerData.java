package common;

import java.io.Serializable;

public class PlayerData implements Serializable {

    private String nickname;
    private boolean isAlive;
    private boolean isConnected;
    private int gamesWon;
    private int ID;
    private int speedRounds;
    private int ghostRounds;

    public PlayerData(String nickname, boolean isAlive, boolean isConnected, int ID, int gamesWon, int speedRounds, int ghostRounds) {
        this.nickname = nickname;
        this.isAlive = isAlive;
        this.isConnected = isConnected;
        this.ID = ID;
        this.gamesWon = gamesWon;
        this.speedRounds = speedRounds;
        this.ghostRounds = ghostRounds;
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

    public int getID() {
        return ID;
    }

    public int getGamesWon() { return gamesWon; }

    public void setGamesWon(int gamesWon) { this.gamesWon = gamesWon; }
}
