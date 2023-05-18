package com.pio.floorislavafront;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class FloorIsLavaApp extends Application {
    private static Stage primaryStage;

    private static void setPrimaryStage(Stage stage) {
        FloorIsLavaApp.primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return FloorIsLavaApp.primaryStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        setPrimaryStage(stage);
        FloorIsLavaController.setScene("initial-screen-scene.fxml");
        stage.setTitle("Floor is Lava!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}