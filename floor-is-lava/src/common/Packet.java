package common;

import java.io.Serializable;
import java.util.ArrayList;


public class Packet implements Serializable {
    private final byte[] map;
    private final int timer;
    private final ArrayList<PlayerData> playerData;
    private final boolean isWaitingForPlayers;
    private final String winnerNickname;

    public Packet(byte[] map, int timer, ArrayList<PlayerData> playerData, boolean isWaitingForPlayers, String winnerNickname) {
        this.map = map;
        this.timer = timer;
        this.playerData = playerData;
        this.isWaitingForPlayers = isWaitingForPlayers;
        this.winnerNickname = winnerNickname;
    }

    public String getWinnerNickname() {
        return winnerNickname;
    }
    public byte[] getMap() {
        return map;
    }

    public int getTimer() {
        return timer;
    }

    public ArrayList<PlayerData> getPlayerData() {
        return playerData;
    }

    public boolean isWaitingForPlayers() {
        return isWaitingForPlayers;
    }
}
