package common;

import back.Position;

public class PowerUp {
    public char type;
    public Position position;

    public PowerUp(char type, Position position) {
        this.type = type;
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
