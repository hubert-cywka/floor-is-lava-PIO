package common;
import front.main.java.com.pio.floorislavafront.DisplayUtils.FieldType;

import java.io.Serializable;

public class Packet implements Serializable {
    public String message;
    public FieldType[][] map;

    public Packet(String message, FieldType[][] map){
        this.message = message;
        this.map = map;
    }

    //TESTING METHOD
    public void printMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
}
