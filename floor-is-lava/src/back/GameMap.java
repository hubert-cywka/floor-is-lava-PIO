package back;

import common.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static back.Game.getRandomNumberInRange;
import static common.GlobalSettings.*;

public class GameMap implements Serializable {

    private final int width;
    private final int height;
    private final FieldType[][] map;
    private Game game;


    public GameMap(Game game) {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.map = new FieldType[HEIGHT][WIDTH];
        this.game = game;
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

    public void generateLavaBorders(int floodStage) {
        int additionalSize = game.getRound() / ROUNDS_TO_INCREASE_LAVA_SIZE;
        int minSize = MIN_LAVA_RADIUS + additionalSize + floodStage;
        int maxSize = MAX_LAVA_RADIUS + additionalSize + floodStage;

        for (int row = 0; row < height; row++) {
            int randomSeed = getRandomNumberInRange(0, BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                insertZone(0, row, getRandomNumberInRange(minSize, maxSize), getRandomNumberInRange(minSize, maxSize), FieldType.LAVA);
                insertZone(width - 1, row, getRandomNumberInRange(minSize, maxSize), getRandomNumberInRange(minSize, maxSize), FieldType.LAVA);
            }
        }

        for (int col = 0; col < width; col++) {
            int randomSeed = getRandomNumberInRange(0, BORDER_HOLE_FREQUENCY);
            if (randomSeed == 0) {
                insertZone(col, 0, getRandomNumberInRange(minSize, maxSize), getRandomNumberInRange(minSize, maxSize), FieldType.LAVA);
                insertZone(col, height - 1, getRandomNumberInRange(minSize, maxSize), getRandomNumberInRange(minSize, maxSize), FieldType.LAVA);
            }
        }
    }

    public void setLavaTime() {
        replaceFields(FieldType.FLOOR, FieldType.LAVA);
    }

    public void setSafeTime() {
        ArrayList<FieldType> fieldsToReplace = new ArrayList<>(Arrays.asList(FieldType.LAVA, FieldType.SAFE_ZONE));
        replaceFields(fieldsToReplace, FieldType.FLOOR);

        for (Player player : game.playersList)
            player.setLastStandingField(FieldType.FLOOR);

        generateLavaBorders(0);

        int safeZonesAmount = Math.max(MAXIMUM_SAFE_ZONES - game.getRound() / ROUNDS_TO_DECREASE_SAFE_ZONES_COUNT, MINIMUM_SAFE_ZONES);
        generateSafeZones(safeZonesAmount);
    }

    public void generateHoles() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int randomSeed = getRandomNumberInRange(0, MAP_HOLE_FREQUENCY);
                if (randomSeed == 0) {
                    int randomWidth = getRandomNumberInRange(MIN_HOLE_RADIUS, MAX_HOLE_RADIUS);
                    int randomHeight = getRandomNumberInRange(MIN_HOLE_RADIUS, MAX_HOLE_RADIUS);
                    insertZone(col, row, randomWidth, randomHeight, FieldType.HOLE);
                }
            }
        }
    }

    public void generateSafeZones(int number) {
        int safeZones = 0, row, col;

        while (safeZones < number) {

            row = getRandomNumberInRange(0, height - 1);
            col = getRandomNumberInRange(0, width - 1);

            if (map[row][col] != FieldType.FLOOR)
                continue;

            insertZone(col, row, getRandomNumberInRange(MIN_SAFEZONE_SIZE, MAX_SAFEZONE_SIZE), getRandomNumberInRange(MIN_SAFEZONE_SIZE, MAX_SAFEZONE_SIZE), FieldType.SAFE_ZONE);
            safeZones++;
        }

    }

    private void replaceFields(ArrayList<FieldType> from, FieldType to) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (from.contains(map[row][col])) {
                    map[row][col] = to;
                }
            }
        }
    }

    private void replaceFields(FieldType from, FieldType to) {
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

        generateLavaBorders(0);
        generateHoles();
        generateSafeZones(3);
    }

    private boolean isWithinCircle(int col, int row, int centerCol, int centerRow, int radiusSquared) {
        int distanceSquared = (col - centerCol) * (col - centerCol) + (row - centerRow) * (row - centerRow);
        return distanceSquared <= radiusSquared;
    }

    public boolean isOneOf(ArrayList<FieldType> fields, int col, int row) {
        return fields.contains(map[row][col]);
    }

    public boolean isThatField(FieldType field, int col, int row) {
        return map[row][col] == field;
    }

    public void insertZone(int centerCol, int centerRow, int radiusWidth, int radiusHeight, FieldType tile) {
        int radiusSquared = radiusWidth * radiusHeight;
        int startCol = centerCol - radiusWidth;
        int startRow = centerRow - radiusHeight;
        int endCol = centerCol + radiusWidth;
        int endRow = centerRow + radiusHeight;

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {

                if (isValidPosition(col, row) && isWithinCircle(col, row, centerCol, centerRow, radiusSquared) && !isOneOf(RESTRICTED_FIELDS, col, row)) {

                    actionWhenSafezoneOnPlayer(row, col, tile);

                    if (!killPlayerInLava(row, col, tile))
                        continue;

                    map[row][col] = tile;

                }


            }
        }
    }


    private void actionWhenSafezoneOnPlayer(int row, int col, FieldType tile) {
        if (PLAYER_FIELDS.contains(map[row][col]) && (tile == FieldType.SAFE_ZONE)) {
            Player player = game.findPlayerByFiledType(map[row][col]);

            if (player == null)
                return;

            player.setLastStandingField(FieldType.SAFE_ZONE);
        }
    }

    private boolean killPlayerInLava(int row, int col, FieldType tile) {

        if (PLAYER_FIELDS.contains(map[row][col]) && tile == FieldType.LAVA) {
            Player player = game.findPlayerByFiledType(map[row][col]);

            if (player == null)
                return true;

            if (player.getLastStandingField() == FieldType.SAFE_ZONE)
                return false;

            if (player != null)
                game.killPlayer(player);
        }

        return true;
    }

    public boolean checkIfFloor(Position pos) {
        try {
            return map[pos.col][pos.row] == FieldType.FLOOR;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

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

    public void removePlayerFromMap(Player player) {
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

    private FieldType getPlayerSymbol(Player player) {
        return FieldType.valueOf("PLAYER_" + player.getID());
    }

    private void renewPlayerSymbol(Player player) {
        FieldType symbol = getPlayerSymbol(player);
        Position position = player.getPosition();
        map[position.row][position.col] = symbol;
    }

    public void movePlayer(Player player, Direction move) {

        if (!player.isAlive())
            return;

        Position position = player.getPosition();
        FieldType playerSymbol = getPlayerSymbol(player);
        renewPlayerSymbol(player);

        Position newPosition = new Position(position.col, position.row);
        switch (move) {
            case UP -> newPosition.row--;
            case DOWN -> newPosition.row++;
            case RIGHT -> newPosition.col++;
            case LEFT -> newPosition.col--;
        }

        if (isCollision(newPosition)) return;

        if (isThatField(FieldType.LAVA, newPosition.col, newPosition.row)) {
            game.killPlayer(player);
            return;
        }

        if (isThatField(FieldType.BOOST_SPEED, newPosition.col, newPosition.row)) {
            player.setRoundsBoostedSpeed(BOOSTER_ROUNDS);
        }

        if (isThatField(FieldType.BOOST_GHOST, newPosition.col, newPosition.row)) {
            player.setRoundsBoostedGhost(BOOSTER_ROUNDS);
        }

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

    private boolean isCollision(Position newPosition) {
        return isPlayerCollision(newPosition) || isInHole(newPosition) || isOutOfBorder(newPosition);
    }

    private boolean isPlayerCollision(Position newPosition) {
        FieldType fieldType = map[newPosition.row][newPosition.col];
        return PLAYER_FIELDS.contains(fieldType);
    }

    private boolean isInHole(Position newPosition) {
        FieldType fieldType = map[newPosition.row][newPosition.col];
        return fieldType == FieldType.HOLE;
    }
}
