package front.main.java.com.pio.floorislavafront;

import front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler;
import front.main.java.com.pio.floorislavafront.soundtrack.SoundtrackManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class FloorIsLavaApp extends Application {
    private static Stage primaryStage;
    private static final SoundtrackManager soundtrackManager = new SoundtrackManager();

    private static void setPrimaryStage(Stage stage) {
        FloorIsLavaApp.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return FloorIsLavaApp.primaryStage;
    }

    public static SoundtrackManager getSoundtrackManager() {
        return soundtrackManager;
    }

    @Override
    public void start(Stage stage) {
        setPrimaryStage(stage);
        DisplayHandler.initTextures();
        FloorIsLavaController.setScene("scenes/initial-screen-scene.fxml");
        stage.setTitle("Floor is Lava!");
        stage.show();
    }

    @Override
    public void stop() {
        try {
            FloorIsLavaController.leaveGame();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }

}