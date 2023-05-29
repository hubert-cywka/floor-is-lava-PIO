package back;

import common.Direction;
import common.FieldType;
import common.Player;
import common.PowerUp;

import java.io.Serializable;
import java.util.Arrays;

import static back.Game.getRandomNumberInRange;
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

    public void addLavaPatch(int col, int row) {
        int randomWidth = (int) Math.floor(Math.random() * (MAX_LAVA_RADIUS - MIN_LAVA_RADIUS) + MIN_LAVA_RADIUS);
        int randomHeight = (int) Math.floor(Math.random() * (MAX_LAVA_RADIUS - MIN_LAVA_RADIUS) + MIN_LAVA_RADIUS);
        insertZone(col, row, randomWidth, randomHeight, FieldType.LAVA);

    }

    public void generateLavaBorders() {
        for (int row = 0; row < height; row++) {
            int randomSeed = (int) Math.floor(Math.random() * BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                addLavaPatch(0, row);
                addLavaPatch(width - 1, row);
            }
        }

        for (int col = 0; col < width; col++) {
            int randomSeed = (int) Math.floor(Math.random() * BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                addLavaPatch(col, 0);
                addLavaPatch(col, height - 1);
            }
        }
    }

    public void setLavaTime(){
        replaceFields(FieldType.FLOOR, FieldType.LAVA);
    }

    public void setSafeTime(){
        replaceFields(FieldType.LAVA, FieldType.FLOOR);
        replaceFields(FieldType.SAFE_ZONE, FieldType.FLOOR);
        generateLavaBorders();
        generateSafeZones(2);
    }

    public void generateSafeZones(int number){

        int safeZones = 0, row, col;

        while(safeZones < number){

            row = getRandomNumberInRange(0, height-1);
            col = getRandomNumberInRange(0, width-1);

            if (map[row][col] != FieldType.FLOOR)
                continue;

            insertZone(col, row, getRandomNumberInRange(4, 10), getRandomNumberInRange(4, 10), FieldType.SAFE_ZONE);
            safeZones++;

        }

//        for (int row = 0; row < height; row++) {
//            for (int col = 0; col < width; col++) {
//
//                if (safeZones >= number)
//                    return;
//
//                int randomSeed = (int) Math.floor(Math.random() * 200);
//                if (randomSeed == 0) {
//                    int randomWidth = (int) Math.floor(Math.random() * (MAX_HOLE_RADIUS - MIN_HOLE_RADIUS) + MAX_HOLE_RADIUS);
//                    int randomHeight = (int) Math.floor(Math.random() * (MAX_HOLE_RADIUS - MIN_HOLE_RADIUS) + MAX_HOLE_RADIUS);
//                    insertZone(col, row, randomWidth, randomHeight, FieldType.SAFE_ZONE);
//                    safeZones++;
//                }
//            }
//        }

    }

    private void replaceFields(FieldType from, FieldType to){

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (map[row][col] == from) {
                    map[row][col] = to;
                }
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
                    insertZone(col, row, randomWidth, randomHeight, FieldType.HOLE);
                }
            }
        }
    }

    private boolean isWithinCircle(int col, int row, int centerCol, int centerRow, int radiusSquared) {
        int distanceSquared = (col - centerCol) * (col - centerCol) + (row - centerRow) * (row - centerRow);
        return distanceSquared <= radiusSquared;
    }

    public void insertZone(int centerCol, int centerRow, int radiusWidth, int radiusHeight, FieldType tile) {
        int radiusSquared = radiusWidth * radiusHeight;
        int startCol = centerCol - radiusWidth;
        int startRow = centerRow - radiusHeight;
        int endCol = centerCol + radiusWidth;
        int endRow = centerRow + radiusHeight;

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (isValidPosition(col, row) && isWithinCircle(col, row, centerCol, centerRow, radiusSquared)) {
                    map[row][col] = tile;
                }
            }
        }
    }

    public boolean checkIfFloor(Position pos) {
        try {
            return map[pos.col][pos.row] == FieldType.FLOOR;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    // szybkosc przenikanie przez sciany
    public void insertPowerUp(PowerUp pow) {
        if (isValidPosition(pow.position.col, pow.position.row) && checkIfFloor(pow.position))
            map[pow.position.col][pow.position.row] = pow.type;
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
        if (isValidPosition(p.position)) {
            map[p.position.row][p.position.col] = getPlayerTile(p.getID());
        }
    }

    public void removePlayer(Player player) {
        Position position = player.getPosition();
        map[position.row][position.col] = player.getLastStandingField();
    }

    private boolean isValidPosition(int col, int row) {
        if (row < 0 || row >= height || col < 0 || col >= width) return false;
        return map[row][col] != FieldType.LAVA && map[row][col] != FieldType.HOLE;
    }

    private boolean isValidPosition(Position p) {
        if (p.col < 0 || p.col >= width || p.row < 0 || p.row >= height) return false;
        return map[p.row][p.col] != FieldType.LAVA && map[p.row][p.col] != FieldType.HOLE;
    }

    private boolean isFloor(int col, int row) {
        return isValidPosition(col, row) && map[row][col] == FieldType.FLOOR;
    }

    public void movePlayer(Player player, Direction move) {

        Position position = player.getPosition();
        FieldType playerSymbol = map[position.row][position.col];

        Position newPosition = new Position(position.col, position.row);
        switch (move) {
            case UP -> newPosition.row--;
            case DOWN -> newPosition.row++;
            case RIGHT -> newPosition.col++;
            case LEFT -> newPosition.col--;
        }

        if (isOutOfBorder(newPosition))
            return;

        updateLastStandingFieldOnMap(player);

        player.setLastStandingField(map[newPosition.row][newPosition.col]);
        map[newPosition.row][newPosition.col] = playerSymbol;
        player.setPosition(newPosition);

    }

    private boolean isOutOfBorder(Position position) {
        try {
            map[position.row][position.col] = map[position.row][position.col];
            return false;
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }

    private void updateLastStandingFieldOnMap(Player player) {

        FieldType lastPlayerField = player.getLastStandingField();
        Position position = player.getPosition();

        if (lastPlayerField == FieldType.BOOST_SPEED || lastPlayerField == FieldType.BOOST_GHOST)
            map[position.row][position.col] = FieldType.FLOOR;
        else
            map[position.row][position.col] = lastPlayerField;

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
