package common;

import java.io.Serializable;

public class Packet implements Serializable {
    private final byte[] map;
    private final int timer;


    public Packet(byte[] map, int timer) {
        this.map = map;
        this.timer = timer;
    }

    public byte[] getMap() {
        return map;
    }

    public int getTimer() {
        return timer;
    }
}
