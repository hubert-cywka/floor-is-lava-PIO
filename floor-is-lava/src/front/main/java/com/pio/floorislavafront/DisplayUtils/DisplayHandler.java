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


    private static Rectangle createSquare(FieldType fieldType) {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        switch (fieldType) {
            case PLAYER_0:
                square.setFill(Color.DARKBLUE);
                square.setStroke(Color.GRAY);
                break;

            case PLAYER_1:
                square.setFill(Color.BLUE);
                square.setStroke(Color.GRAY);
                break;

            case PLAYER_2:
                square.setFill(Color.BLUE);
                square.setStroke(Color.GRAY);
                break;

            case PLAYER_3:
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

            case SAFE_ZONE:
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

            case BOOST_SPEED:
                square.setFill(Color.LIGHTYELLOW);
                square.setStroke(Color.GRAY);
                break;

            case BOOST_GHOST:
                square.setFill(Color.LIGHTBLUE);
                square.setStroke(Color.GRAY);
                break;
        }
        return square;
    }

    private static FieldType fieldTypeChecker(char c) {
        return switch (c) {
            case '0' -> FieldType.PLAYER_0;
            case '1' -> FieldType.PLAYER_1;
            case '2' -> FieldType.PLAYER_2;
            case '3' -> FieldType.PLAYER_3;
            case '#' -> FieldType.WALL;
            case '$' -> FieldType.BORDER;
            case '*' -> FieldType.SAFE_ZONE;
            case '.' -> FieldType.FLOOR;
            case '@' -> FieldType.LAVA;
            case 'x' -> FieldType.BOOST_SPEED;
            case 'y' -> FieldType.BOOST_GHOST;
            default -> FieldType.WALL;
        };
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
