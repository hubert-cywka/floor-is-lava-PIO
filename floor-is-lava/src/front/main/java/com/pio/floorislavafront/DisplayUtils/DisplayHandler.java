package front.main.java.com.pio.floorislavafront.DisplayUtils;

import common.FieldType;
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
            case PLAYER_0 -> square.setFill(Color.BLUE);
            case PLAYER_1 -> square.setFill(Color.BLUE);
            case PLAYER_2 -> square.setFill(Color.BLUE);
            case PLAYER_3 -> square.setFill(Color.BLUE);
            case WALL -> square.setFill(Color.YELLOW);
            case BORDER -> square.setFill(Color.BLACK);
            case SAFE_ZONE -> square.setFill(Color.LIGHTGREEN);
            case FLOOR -> square.setFill(Color.DARKGREEN);
            case LAVA -> square.setFill(Color.DARKRED);
            case HOLE -> square.setFill(Color.WHITE);
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

            for (int y = 0; y < MAP_SIZE; y++) {
                for (int x = 0; x < MAP_SIZE; x++) {
                    Rectangle square = createSquare(map[y][x]);
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
