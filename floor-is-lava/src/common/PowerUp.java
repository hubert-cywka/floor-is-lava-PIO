package common;

import back.Position;

public class PowerUp {
    public FieldType type;
    public Position position;

    public PowerUp(FieldType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
