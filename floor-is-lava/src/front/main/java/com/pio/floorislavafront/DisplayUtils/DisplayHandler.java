package front.main.java.com.pio.floorislavafront.DisplayUtils;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class DisplayHandler {

    private static final int SQUARE_SIZE = 5;
    private static final int MAP_SIZE = 100;
    private static final int PLAYER = 0;
    private static final int WALL = 1;
    private static final int BORDER = 2;
    private static final int SAFEZONE = 3;
    private static final int FLOOR = 4;
    private static final int LAVA = 5;

    private static Rectangle createSquare(int fieldtype) {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        switch (fieldtype) {
            case PLAYER:
                square.setFill(Color.BLUE);
                square.setStroke(Color.GRAY);
                break;
            case WALL:
                square.setFill(Color.YELLOW);
                square.setStroke(Color.GRAY);
                break;
            case BORDER:
                square.setFill(Color.BLACK);
                square.setStroke(Color.GRAY);
                break;
            case SAFEZONE:
                square.setFill(Color.LIGHTGREEN);
                square.setStroke(Color.BLACK);
                break;
            case FLOOR:
                square.setFill(Color.DARKGREEN);
                square.setStroke(Color.GRAY);
                break;
            case LAVA:
                square.setFill(Color.DARKRED);
                square.setStroke(Color.GRAY);
                break;
        }
        return square;
    }

    private static int fieldTypeChecker(char c) {
        switch (c) {
            case '0':
                return PLAYER;
            case '#':
                return WALL;
            case '$':
                return BORDER;
            case '*':
                return SAFEZONE;
            case '.':
                return FLOOR;
            case '@':
                return LAVA;
            default:
                return -1;
        }
    }

    public static void mapHandler(char[][] map) {
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "gamescene";
        Node container = currentScene.lookup("#" + containerId);

        if (container instanceof AnchorPane) {
            AnchorPane myContainer = (AnchorPane) container;
//            System.out.println("Znaleziono kontener o ID: " + containerId);

            Node mapNode = container.lookup("#mymap");
            if (mapNode instanceof GridPane)
            {
                GridPane writtenMap = (GridPane) mapNode;
                myContainer.getChildren().remove(writtenMap);
            }

            GridPane myMap = new GridPane();
            myMap.setId("mymap");

            for (int y = 0; y < MAP_SIZE; y++) {
                for (int x = 0; x < MAP_SIZE; x++) {
                    Rectangle square = createSquare(fieldTypeChecker(map[y][x]));
                    myMap.add(square, x, y);
                }
            }

            AnchorPane.setTopAnchor(myMap, (myContainer.getHeight() - myMap.getHeight()) / 4);
            AnchorPane.setLeftAnchor(myMap, (myContainer.getWidth() - myMap.getWidth()) / 3);
            myContainer.getChildren().add(myMap);


        } else {
            System.err.println("Nie znaleziono kontenera o ID: " + containerId);
        }
    }
}
