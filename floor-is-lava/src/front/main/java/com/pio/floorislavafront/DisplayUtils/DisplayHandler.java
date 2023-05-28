package front.main.java.com.pio.floorislavafront.DisplayUtils;

import common.FieldType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static common.GlobalSettings.*;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class DisplayHandler {
    private static final String SPRITE_IMAGE_BASE = "src/front/main/resources/com/pio/floorislavafront/images/sprites/";
    private static final InputStream SAFE_ZONE_SPRITE_IMAGE;
    private static final InputStream LAVA_SPRITE_IMAGE;
    private static final InputStream HOLE_SPRITE_IMAGE;
    private static final InputStream FLOOR_SPRITE_IMAGE;
    private static final InputStream PLAYER_SPRITE_RED_IMAGE;
    private static final InputStream PLAYER_SPRITE_BLACK_IMAGE;
    private static final InputStream PLAYER_SPRITE_PURPLE_IMAGE;
    private static final InputStream PLAYER_SPRITE_BLUE_IMAGE;

    static {
        try {
            PLAYER_SPRITE_RED_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("red.png"));
            PLAYER_SPRITE_PURPLE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("purple.png"));
            PLAYER_SPRITE_BLACK_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("black.png"));
            PLAYER_SPRITE_BLUE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("blue.png"));
            FLOOR_SPRITE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("floor.png"));
            SAFE_ZONE_SPRITE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("safe_zone.png"));
            LAVA_SPRITE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("lava.png"));
            HOLE_SPRITE_IMAGE = new FileInputStream(SPRITE_IMAGE_BASE.concat("hole.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ImagePattern PLAYER_SPRITE_BLUE = new ImagePattern(new Image(PLAYER_SPRITE_BLUE_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_RED = new ImagePattern(new Image(PLAYER_SPRITE_RED_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_BLACK = new ImagePattern(new Image(PLAYER_SPRITE_BLACK_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_PURPLE = new ImagePattern(new Image(PLAYER_SPRITE_PURPLE_IMAGE));
    private static final ImagePattern FLOOR_SPRITE = new ImagePattern(new Image(FLOOR_SPRITE_IMAGE));
    private static final ImagePattern SAFE_ZONE_SPRITE = new ImagePattern(new Image(SAFE_ZONE_SPRITE_IMAGE));
    private static final ImagePattern LAVA_SPRITE = new ImagePattern(new Image(LAVA_SPRITE_IMAGE));
    private static final ImagePattern HOLE_SPRITE = new ImagePattern(new Image(HOLE_SPRITE_IMAGE));

    private static Rectangle createSquare(FieldType fieldType) {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

        switch (fieldType) {
            case PLAYER_0 -> square.setFill(PLAYER_SPRITE_BLUE);
            case PLAYER_1 -> square.setFill(PLAYER_SPRITE_RED);
            case PLAYER_2 -> square.setFill(PLAYER_SPRITE_BLACK);
            case PLAYER_3 -> square.setFill(PLAYER_SPRITE_PURPLE);
            case WALL -> square.setFill(Color.YELLOW);
            case BORDER -> square.setFill(Color.BLACK);
            case SAFE_ZONE -> square.setFill(SAFE_ZONE_SPRITE);
            case FLOOR -> square.setFill(FLOOR_SPRITE);
            case LAVA -> square.setFill(LAVA_SPRITE);
            case HOLE -> square.setFill(HOLE_SPRITE);
            case BOOST_SPEED -> square.setFill(Color.LIGHTYELLOW);
            case BOOST_GHOST -> square.setFill(Color.LIGHTBLUE);
        }
        return square;
    }


    public static void mapHandler(FieldType[][] map) {
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "gamescene";
        Node container = currentScene.lookup("#" + containerId);

        if (container instanceof AnchorPane) {
            AnchorPane myContainer = (AnchorPane) container;
//            System.out.println("Znaleziono kontener o ID: " + containerId);

            Node mapNode = container.lookup("#mymap");
            if (mapNode instanceof GridPane) {
                GridPane writtenMap = (GridPane) mapNode;
                myContainer.getChildren().remove(writtenMap);
            }

            GridPane myMap = new GridPane();
            myMap.setId("mymap");

            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    Rectangle square = createSquare(map[row][col]);
                    myMap.add(square, col, row);
                }
            }

            AnchorPane.setTopAnchor(myMap, (myContainer.getHeight() / 2 - (SQUARE_SIZE * HEIGHT) / 2));
            AnchorPane.setLeftAnchor(myMap, (myContainer.getWidth() / 2 - (SQUARE_SIZE * WIDTH) / 2));
            myContainer.getChildren().add(myMap);


        } else {
            System.err.println("Nie znaleziono kontenera o ID: " + containerId);
        }
    }
}
