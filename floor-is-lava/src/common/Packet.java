package common;

import java.io.Serializable;

public class Packet implements Serializable {
    private final byte[] map;


    public Packet(byte[] map) {
        this.map = map;
    }

    public byte[] getMap() {
        return map;
    }
}
