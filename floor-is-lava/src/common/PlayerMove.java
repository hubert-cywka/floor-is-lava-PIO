package common;

import java.io.Serializable;

public class PlayerMove implements Serializable {
    private Direction horizontal;
    private Direction vertical;

    public PlayerMove(Direction horizontal, Direction vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Direction getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Direction horizontal) {
        this.horizontal = horizontal;
    }

    public Direction getVertical() {
        return vertical;
    }

    public void setVertical(Direction vertical) {
        this.vertical = vertical;
    }
}
