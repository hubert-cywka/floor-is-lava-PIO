package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.FieldType;
import common.Packet;
import common.Player;
import common.PlayerData;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler.gameHandler;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class DataTransferThread implements Runnable {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public DataTransferThread(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        Player.setConnected(true);

        try {
            while (Player.isConnected()) {
                Packet packet = receiveData();
                sendPlayerMove();
                updateMap(packet);
            }

        } catch (IOException | ClassNotFoundException e) {
            displayConnectionLost();
            throw new RuntimeException(e);
        }

        try {
            objectOutputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void displayConnectionLost(){
        Scene currentScene = getPrimaryStage().getScene();
        String containerId = "gameMessage";
        Node container = currentScene.lookup("#" + containerId);
        if (container instanceof Label) {
            Label messageLabel = (Label) container;
            messageLabel.setText("Server connection lost");
        }
    }
    public static FieldType[][] deserializeFieldTypeArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (FieldType[][]) objectStream.readObject();
    }

    private Packet receiveData() throws IOException, ClassNotFoundException {
        return (Packet) objectInputStream.readObject();
    }

    private void sendPlayerMove() throws IOException {
        objectOutputStream.writeObject(Player.getNextPlayerMove());
        objectOutputStream.reset();
    }

    private FieldType[][] getDeserializedMap(byte[] serializedMap) throws IOException, ClassNotFoundException {
        return deserializeFieldTypeArray(serializedMap);
    }

    private void updateMap(Packet packet) throws IOException, ClassNotFoundException {
        FieldType[][] map = getDeserializedMap(packet.getMap());
        Platform.runLater(() -> gameHandler(map, packet.getTimer(), packet.getPlayerData(), packet.isWaitingForPlayers()));
    }
}