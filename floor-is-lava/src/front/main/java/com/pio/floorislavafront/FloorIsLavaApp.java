package front.main.java.com.pio.floorislavafront;

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
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FloorIsLavaController.setScene("scenes/initial-screen-scene.fxml");
        stage.setTitle("Floor is Lava!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}