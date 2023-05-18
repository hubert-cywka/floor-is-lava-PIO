package com.pio.floorislavafront;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

public class FloorIsLavaController {
    private final String INITIAL_SCREEN = "initial-screen-scene.fxml";
    private final String INSTRUCTIONS_SCREEN = "instructions-scene.fxml";

    public static void setScene(String sceneFXML) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(FloorIsLavaApp.class.getResource(sceneFXML));
        try {
            FloorIsLavaApp.getPrimaryStage().setScene(new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight() - 20));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onQuitButtonClick() {
        Platform.exit();
    }

    @FXML
    protected void onInstructionsButtonClick() {
        setScene(INSTRUCTIONS_SCREEN);
    }

    @FXML
    protected void onGoBackButtonClick() {
        setScene(INITIAL_SCREEN);
    }
}