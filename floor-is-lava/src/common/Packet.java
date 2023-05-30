package common;

import java.io.Serializable;
import java.util.ArrayList;


public class Packet implements Serializable {
    private final byte[] map;
    private final int timer;
    private final ArrayList<PlayerData> playerData;


    public Packet(byte[] map, int timer, ArrayList<PlayerData> playerData) {
        this.map = map;
        this.timer = timer;
        this.playerData = playerData;
    }


    public byte[] getMap() {
        return map;
    }

    public int getTimer() {
        return timer;
    }
}
