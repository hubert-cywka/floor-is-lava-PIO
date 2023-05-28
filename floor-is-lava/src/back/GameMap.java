package back;

import common.Direction;
import common.FieldType;
import common.Player;
import common.PowerUp;

import java.io.Serializable;
import java.util.Arrays;

import static common.GlobalSettings.*;

public class GameMap implements Serializable {

    private final int width;
    private final int height;
    private final FieldType[][] map;

    public GameMap() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.map = new FieldType[HEIGHT][WIDTH];
        generateMap();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public FieldType[][] getMap() {
        return map;
    }

    public void addLavaPatch(int x, int y) {
        int randomWidth = (int) Math.floor(Math.random() * (MAX_LAVA_RADIUS - MIN_LAVA_RADIUS) + MIN_LAVA_RADIUS);
        int randomHeight = (int) Math.floor(Math.random() * (MAX_LAVA_RADIUS - MIN_LAVA_RADIUS) + MIN_LAVA_RADIUS);
        insertZone(x, y, randomWidth, randomHeight, FieldType.LAVA);

    }

    public void generateLavaBorders() {
        for (int row = 0; row < height; row++) {
            int randomSeed = (int) Math.floor(Math.random() * BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                addLavaPatch(row, 0);
                addLavaPatch(row, width - MIN_HOLE_RADIUS);
            }
        }

        for (int col = 0; col < width; col++) {
            int randomSeed = (int) Math.floor(Math.random() * BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                addLavaPatch(0, col);
                addLavaPatch(height - MIN_HOLE_RADIUS, col);
            }
        }
    }

    public void generateMap() {
        for (int row = 0; row < height; row++) {
            Arrays.fill(map[row], FieldType.FLOOR);
        }

        generateLavaBorders();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int randomSeed = (int) Math.floor(Math.random() * MAP_HOLE_FREQUENCY);
                if (randomSeed == 0) {
                    int randomWidth = (int) Math.floor(Math.random() * (MAX_HOLE_RADIUS - MIN_HOLE_RADIUS) + MAX_HOLE_RADIUS);
                    int randomHeight = (int) Math.floor(Math.random() * (MAX_HOLE_RADIUS - MIN_HOLE_RADIUS) + MAX_HOLE_RADIUS);
                    insertZone(row, col, randomWidth, randomHeight, FieldType.HOLE);
                }
            }
        }
    }

    private boolean isWithinCircle(int x, int y, int centerX, int centerY, int radiusSquared) {
        int distanceSquared = (x - centerX) * (x - centerX) + (y - centerY) * (y - centerY);
        return distanceSquared <= radiusSquared;
    }

    public void insertZone(int centerX, int centerY, int radiusWidth, int radiusHeight, FieldType tile) {
        int radiusSquared = radiusWidth * radiusHeight;
        int startX = centerX - radiusWidth;
        int startY = centerY - radiusHeight;
        int endX = centerX + radiusWidth;
        int endY = centerY + radiusHeight;

        for (int i = startY; i <= endY; i++) {
            for (int j = startX; j <= endX; j++) {
                if (isValidPosition(i, j) && isWithinCircle(j, i, centerX, centerY, radiusSquared)) {
                    map[i][j] = tile;
                }
            }
        }
    }

    public boolean checkIfFloor(Position pos) {
        return map[pos.x][pos.y] == FieldType.FLOOR;
    }

    // szybkosc przenikanie przez sciany
    public void insertPowerUp(PowerUp pow) {
        if (isValidPosition(pow.position.x, pow.position.y) && checkIfFloor(pow.position))
            map[pow.position.x][pow.position.y] = pow.type;
    }

    private FieldType getPlayerTile(int playerId) {
        switch (playerId) {
            case 0 -> {
                return FieldType.PLAYER_0;
            }
            case 1 -> {
                return FieldType.PLAYER_1;
            }
            case 2 -> {
                return FieldType.PLAYER_2;
            }
            case 3 -> {
                return FieldType.PLAYER_3;
            }
            default -> {
                return FieldType.FLOOR;
            }
        }
    }

    public void insertPlayer(Player p) {
        if (isValidPosition(p.position.x, p.position.y)) {
            map[p.position.x][p.position.y] = getPlayerTile(p.getID());
        }
    }

    public void removePlayer(Player p) {
        if (isValidPosition(p.position.x, p.position.y)) {
            map[p.position.x][p.position.y] = getPlayerTile(p.getID());
        }
    }

    private boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return map[x][y] != FieldType.LAVA && map[x][y] != FieldType.HOLE;
    }

    private boolean isFloor(int x, int y) {
        return isValidPosition(x, y) && map[x][y] == FieldType.FLOOR;
    }
    //METHOD FOR TESTING

    public void movePlayer(Player player, Direction move) {

        Position position = player.getPosition();
        System.out.println("First position: " + position);
        FieldType playerSymbol;

        switch (map[position.x][position.y]) {
            case PLAYER_0, PLAYER_1, PLAYER_2, PLAYER_3 -> {
                playerSymbol = map[position.x][position.y];
                System.err.println("Symbol: " + playerSymbol);
                map[position.x][position.y] = player.getLastStandingField();

            }

            default -> {
                System.err.println("There is no player on this position! [GameMap - movePlayer()]");
                return;
            }
        }

        switch (move) {
            case UP -> position.x--;
            case DOWN -> position.x++;
            case RIGHT -> position.y++;

            default -> System.err.println("Unable to handle move like that! [GameMap - movePlayer()]");
        }

        player.setLastStandingField(map[position.x][position.y]);
        map[position.x][position.y] = playerSymbol;

        System.err.println("New pos: " + position);
        player.setPosition(position);

    }

    @Deprecated
    public void printMap() {
        System.err.println("------------------------------------------------------------------------");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.err.println("------------------------------------------------------------------------");
    }

}
