package common;
import java.io.Serializable;

public class Packet implements Serializable {
    public String message;
    public Packet(String message){
        this.message = message;
    }
}
