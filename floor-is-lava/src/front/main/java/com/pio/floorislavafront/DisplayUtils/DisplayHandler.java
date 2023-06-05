package front.main.java.com.pio.floorislavafront.DisplayUtils;

import back.Game;
import back.GameLoop;
import common.FieldType;
import common.PlayerData;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static common.GlobalSettings.*;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class DisplayHandler {
    private static final String SPRITE_IMAGE_BASE = "src/front/main/resources/com/pio/floorislavafront/images/sprites/";
    private static final String STATS_IMAGE_BASE = "src/front/main/resources/com/pio/floorislavafront/images/stats/";
    private static final ArrayList<InputStream> SAFE_ZONE_SPRITE_IMAGE = new ArrayList<>();
    private static final ArrayList<InputStream> LAVA_SPRITE_IMAGE = new ArrayList<>();
    private static final ArrayList<InputStream> HOLE_SPRITE_IMAGE = new ArrayList<>();
    private static final ArrayList<InputStream> FLOOR_SPRITE_IMAGE = new ArrayList<>();
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

            FLOOR_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("floor1.png")));
            FLOOR_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("floor2.png")));
            FLOOR_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("floor3.png")));
            FLOOR_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("floor4.png")));

            SAFE_ZONE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("safe_zone1.png")));
            SAFE_ZONE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("safe_zone2.png")));
            SAFE_ZONE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("safe_zone3.png")));
            SAFE_ZONE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("safe_zone4.png")));

            LAVA_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("lava1.png")));
            LAVA_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("lava2.png")));
            LAVA_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("lava3.png")));
            LAVA_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("lava4.png")));

            HOLE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("hole1.png")));
            HOLE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("hole2.png")));
            HOLE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("hole3.png")));
            HOLE_SPRITE_IMAGE.add(new FileInputStream(SPRITE_IMAGE_BASE.concat("hole4.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ImagePattern PLAYER_SPRITE_BLUE = new ImagePattern(new Image(PLAYER_SPRITE_BLUE_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_RED = new ImagePattern(new Image(PLAYER_SPRITE_RED_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_BLACK = new ImagePattern(new Image(PLAYER_SPRITE_BLACK_IMAGE));
    private static final ImagePattern PLAYER_SPRITE_PURPLE = new ImagePattern(new Image(PLAYER_SPRITE_PURPLE_IMAGE));
    private static final ArrayList<ImagePattern> FLOOR_SPRITES = new ArrayList<>();
    private static final ArrayList<ImagePattern> SAFE_ZONE_SPRITES = new ArrayList<>();
    private static final ArrayList<ImagePattern> LAVA_SPRITES = new ArrayList<>();
    private static final ArrayList<ImagePattern> HOLE_SPRITES = new ArrayList<>();

    public static void initTextures() {
        LAVA_SPRITE_IMAGE.forEach((texture) -> LAVA_SPRITES.add(new ImagePattern(new Image(texture))));
        HOLE_SPRITE_IMAGE.forEach((texture) -> HOLE_SPRITES.add(new ImagePattern(new Image(texture))));
        SAFE_ZONE_SPRITE_IMAGE.forEach((texture) -> SAFE_ZONE_SPRITES.add(new ImagePattern(new Image(texture))));
        FLOOR_SPRITE_IMAGE.forEach((texture) -> FLOOR_SPRITES.add(new ImagePattern(new Image(texture))));
    }

    private static ImagePattern getRandomTextureFromTexturesList(ArrayList<ImagePattern> textures) {
        int random = (int) Math.floor(Math.random() * textures.size());
        return textures.get(random);
    }

    private static ImagePattern getTextureFromTexturesList(ArrayList<ImagePattern> textures, int seed) {
        return textures.get(seed % textures.size());
    }

    private static Rectangle createSquare(FieldType fieldType, int col, int row) {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

        switch (fieldType) {
            case PLAYER_0 -> square.setFill(PLAYER_SPRITE_BLUE);
            case PLAYER_1 -> square.setFill(PLAYER_SPRITE_RED);
            case PLAYER_2 -> square.setFill(PLAYER_SPRITE_BLACK);
            case PLAYER_3 -> square.setFill(PLAYER_SPRITE_PURPLE);
            case WALL -> square.setFill(Color.YELLOW);
            case BORDER -> square.setFill(Color.BLACK);
            case SAFE_ZONE -> square.setFill(getTextureFromTexturesList(SAFE_ZONE_SPRITES, col * 2 + row * 3));
            case FLOOR -> square.setFill(getTextureFromTexturesList(FLOOR_SPRITES, col * 2 + row * 3));
            case LAVA -> square.setFill(getRandomTextureFromTexturesList(LAVA_SPRITES));
            case HOLE -> square.setFill(getRandomTextureFromTexturesList(HOLE_SPRITES));
            case BOOST_SPEED -> square.setFill(Color.LIGHTYELLOW);
            case BOOST_GHOST -> square.setFill(Color.LIGHTBLUE);
        }
        return square;
    }

    private static String buildDisplayedMessage(ArrayList<PlayerData> playerData, boolean isWaitingForPlayers, String winnerNickname) {
        if (winnerNickname!=null) return ("Gracz " + winnerNickname + " won the round!");

        if (isWaitingForPlayers) {
            return "Oczekiwanie na graczy...";
        } else {
            return "Spróbuj przeżyć!";
        }
    }

    public static void displayMessage(String message) {
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "gameMessage";
        Node container = currentScene.lookup("#" + containerId);
        if (container instanceof Label) {
            Label timerLabel = (Label) container;
            timerLabel.setText(message);
        }
    }

    public static void displayTimer(int time){
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "timer";
        Node container = currentScene.lookup("#" + containerId);
        if (container instanceof Label)
        {
            Label timerLabel = (Label) container;
            timerLabel.setText(String.valueOf(time));
        }

    }

    public static void setHeartImage(Node container, boolean isAlive) {
        Image heart;
        if (isAlive) {
            heart = new Image("file:" + STATS_IMAGE_BASE.concat("alive.png"));
        }
        else {
            heart = new Image("file:" + STATS_IMAGE_BASE.concat("dead.png"));
        }
        if (container instanceof ImageView)
        {
            ImageView view = (ImageView) container;
            view.setImage(heart);
        }
    }

    public static void setConnectImage(Node container, boolean isConnected) {
        Image connect;
        if (isConnected) {
            connect = new Image("file:" + STATS_IMAGE_BASE.concat("online.png"));
        }
        else {
            connect = new Image("file:" + STATS_IMAGE_BASE.concat("offline.png"));
        }

        if (container instanceof ImageView)
        {
            ImageView view = (ImageView) container;
            view.setImage(connect);
        }
    }

    public static void setWinsLabel(Node container, int wins) {
        if (container instanceof Label)
        {
            Label winsLabel = (Label) container;
            winsLabel.setText(String.valueOf(wins));
        }
    }

    public static void setNameLabel(Node container, String username) {
        if (container instanceof Label)
        {
            Label nameLabel = (Label) container;
            nameLabel.setText(String.valueOf(username));
        }
    }

    public static void displayPlayerData(ArrayList<PlayerData> playerData){
        Scene currentScene = getPrimaryStage().getScene();
        Node container;
        String imageHeart;
        String imageConnect;
        String nameLabel;
        String winsLabel;

        for (int i=0; i< playerData.size(); i++)
        {
            PlayerData data = playerData.get(i);
            int id = data.getID();

            if (id != -1) {
                imageHeart = "P" + id + "_heart";
                imageConnect = "P" + id + "_connect";
                nameLabel = "P" + id + "_name";
                winsLabel = "P" + id + "_wins";
            }
            else {
                imageHeart = "P" + i + "_heart";
                imageConnect = "P" + i + "_connect";
                nameLabel = "P" + i + "_name";
                winsLabel = "P" + i + "_wins";
            }


            container = currentScene.lookup("#" + imageHeart);
            setHeartImage(container, data.isAlive());

            container = currentScene.lookup("#" + imageConnect);
            setConnectImage(container, data.isConnected());

            container = currentScene.lookup("#" + nameLabel);
            setNameLabel(container, data.getNickname());

            container = currentScene.lookup("#" + winsLabel);
            setWinsLabel(container, data.getGamesWon());
        }

    }

    public static void gameHandler(FieldType[][] map, int time, ArrayList<PlayerData> playerData, boolean isWaitingForPlayers, String winnerNickname) {
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "gamescene";
        Node container = currentScene.lookup("#" + containerId);

        displayTimer(time);
        displayPlayerData(playerData);
        System.out.println("WINNER = " + winnerNickname);
        displayMessage(buildDisplayedMessage(playerData, isWaitingForPlayers, winnerNickname));

        if (container instanceof AnchorPane) {
            AnchorPane myContainer = (AnchorPane) container;

            Node mapNode = container.lookup("#mymap");
            if (mapNode instanceof GridPane) {
                GridPane writtenMap = (GridPane) mapNode;
                myContainer.getChildren().remove(writtenMap);
            }

            GridPane myMap = new GridPane();
            myMap.setId("mymap");

            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    Rectangle square = createSquare(map[row][col], col, row);
                    myMap.add(square, col, row);
                }
            }

            AnchorPane.setBottomAnchor(myMap, 10.0);
            AnchorPane.setLeftAnchor(myMap, (myContainer.getWidth() / 2 - (SQUARE_SIZE * WIDTH) / 2));
            myContainer.getChildren().add(myMap);


        } else {
            System.err.println("Nie znaleziono kontenera o ID: " + containerId);
        }
    }
}