package common;

import java.io.Serializable;

public class Packet implements Serializable {
    public byte[] map;

    public Packet(byte[] map) {
        this.map = map;
    }

    public byte[] getMap() {
        return map;
    }
}
