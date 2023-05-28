package front.main.java.com.pio.floorislavafront.DisplayUtils;

import common.FieldType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static common.GlobalSettings.*;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class DisplayHandler {

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
