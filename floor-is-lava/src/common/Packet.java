package common;

import java.io.Serializable;
import java.util.ArrayList;

import static common.GlobalSettings.MAX_PLAYERS;

public class Packet implements Serializable {
    private final byte[] map;
    private final int timer;
    private final ArrayList<PlayerData> playerData = new ArrayList<>(MAX_PLAYERS);


    public Packet(byte[] map, int timer) {
        this.map = map;
        this.timer = timer;
    }


    public void addPlayerData(PlayerData newData){

        if (playerData.size() >= MAX_PLAYERS)
            return;

        playerData.add(newData);

    }

    public byte[] getMap() {
        return map;
    }

    public int getTimer() {
        return timer;
    }
}
