package back;

import java.io.Serializable;
import java.util.Arrays;

public class GameMap implements Serializable {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private int width;
    private int height;
    private char[][] map;

    public GameMap() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.map = new char[HEIGHT][WIDTH];
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public char[][] getMap() {
        return map;
    }
    public void generateMap() {
        for (int i = 0; i < height; i++) {
            Arrays.fill(map[i], '.');
        }
    }

    //SafePlace might become class later
    public void insertSafePlace(int startX, int startY, int width, int height) {
        for (int i = startY; i < startY + height; i++) {
            for (int j = startX; j < startX + width; j++) {
                if (isValidPosition(j, i)) {
                    map[i][j] = '*';
                }
            }
        }
    }
    public void removeSafePlace(int startX, int startY, int width, int height) {
        for (int i = startY; i < startY + height; i++) {
            for (int j = startX; j < startX + width; j++) {
                if (isValidPosition(j, i)) {
                    map[i][j] = '.';
                }
            }
        }
    }
    public void insertPlayer(Player p) {
        if (isValidPosition(p.position.x, p.position.y)) {
            map[p.position.x][p.position.y] = (char) (p.getID() + 48);
        }
    }
    public void removePlayer(Player p) {
        if (isValidPosition(p.position.x, p.position.y)) {
            map[p.position.x][p.position.y] = (char) (p.getID() + 48);
        }
    }
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    //METHOD FOR TESTING
    public void printMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

}
