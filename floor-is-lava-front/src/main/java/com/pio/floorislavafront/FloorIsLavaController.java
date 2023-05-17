package com.pio.floorislavafront;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class FloorIsLavaController {

    @FXML
    protected void onQuitButtonClick() {
        Platform.exit();
    }
}