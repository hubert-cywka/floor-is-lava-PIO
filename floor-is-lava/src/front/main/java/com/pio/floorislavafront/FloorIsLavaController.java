package front.main.java.com.pio.floorislavafront;

import common.PlayerMove;
import front.main.java.com.pio.floorislavafront.ClientSocket.ConnectionInitializer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.regex.Pattern;

import static common.Player.setNextPlayerMove;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class FloorIsLavaController {
    private static final String INITIAL_SCREEN = "scenes/initial-screen-scene.fxml";
    private static final String INSTRUCTIONS_SCREEN = "scenes/instructions-scene.fxml";
    public static final String GAME_SCREEN = "scenes/game-scene.fxml";
    private final String USERNAME_NOT_VALID = "Nazwa użytkownika nie może pozostać pusta!";
    private final String SERVER_ADDRESS_NOT_VALID = "Adres serwera jest nieprawidłowy. Poprawny format to X.X.X.X:XXXX, np.: 192.168.0.1:8080";
    private final String USERNAME_HELPER_TEXT = "Jak się chcesz nazywać?";
    private final String SERVER_ADDRESS_HELPER_TEXT = "Podaj adres serwera w formacie X.X.X.X:XXXX";
    private final String INPUT_VALIDATION_SUCCESS = "Wszystko OK!";
    private final Pattern SERVER_ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[:]\\d{1,4}$");

    @FXML
    Label joinGameHelperLabel;

    @FXML
    TextField usernameTextField;

    @FXML
    TextField serverTextField;

    private static void initKeyListeners() {
        getPrimaryStage().getScene().setOnKeyPressed((KeyEvent event) -> {

            switch (event.getCode()) {
                case W -> setNextPlayerMove(PlayerMove.UP);
                case A -> setNextPlayerMove(PlayerMove.LEFT);
                case S -> setNextPlayerMove(PlayerMove.DOWN);
                case D -> setNextPlayerMove(PlayerMove.RIGHT);
                case ESCAPE -> System.out.println("ESCAPE"); // TODO handle exit
                case O -> FloorIsLavaApp.getSoundtrackManager().toggleVolume();
                case I -> FloorIsLavaApp.getSoundtrackManager().playPreviousSong();
                case P -> FloorIsLavaApp.getSoundtrackManager().playNextSong();
                case K -> FloorIsLavaApp.getSoundtrackManager().playAmbientPlaylist();
                case L -> FloorIsLavaApp.getSoundtrackManager().playInspiringPlaylist();
                default -> System.out.println("NOT RECOGNIZED KEY");
            }
        });
    }

    public static void setScene(String sceneFXML) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FloorIsLavaApp.class.getResource(sceneFXML));
            getPrimaryStage().setScene(new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight() - 20));
            initKeyListeners();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean validateUserInput() {
        String username = usernameTextField.getText();
        String serverAddress = serverTextField.getText();

        if (username.isBlank()) {
            joinGameHelperLabel.setText(USERNAME_NOT_VALID);
            joinGameHelperLabel.setTextFill(Color.RED);
            return false;
        } else if (!SERVER_ADDRESS_PATTERN.matcher(serverAddress).find() && !serverAddress.contains("localhost")) {
            joinGameHelperLabel.setText(SERVER_ADDRESS_NOT_VALID);
            joinGameHelperLabel.setTextFill(Color.RED);
            return false;
        }

        joinGameHelperLabel.setText(INPUT_VALIDATION_SUCCESS);
        joinGameHelperLabel.setTextFill(Color.LIME);
        return true;
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

    @FXML
    protected void onJoinGameButtonClick() {
        if (!validateUserInput()) return;
        String username = usernameTextField.getText();
        String serverAddress = serverTextField.getText();

        // Processing user data for server connection
        String[] parts = serverAddress.split(":");
        String ipAddress = parts[0];
        String textport = parts[1];
        int port = Integer.parseInt(textport);

        // Connection thread
        Thread connectionThread = new Thread(new ConnectionInitializer(ipAddress, port, username));
        connectionThread.start();

        setScene(GAME_SCREEN);
    }

    @FXML
    protected void onServerAddressTextFieldClick() {
        joinGameHelperLabel.setText(SERVER_ADDRESS_HELPER_TEXT);
        joinGameHelperLabel.setTextFill(Color.WHITE);
    }

    @FXML
    protected void onUsernameTextFieldClick() {
        joinGameHelperLabel.setText(USERNAME_HELPER_TEXT);
        joinGameHelperLabel.setTextFill(Color.WHITE);
    }
}