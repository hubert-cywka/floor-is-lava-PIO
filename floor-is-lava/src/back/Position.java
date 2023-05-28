package back;

import java.io.Serializable;

public class Position implements Serializable {

    public int col;
    public int row;

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + col +
                ", y=" + row +
                '}';
    }
}
