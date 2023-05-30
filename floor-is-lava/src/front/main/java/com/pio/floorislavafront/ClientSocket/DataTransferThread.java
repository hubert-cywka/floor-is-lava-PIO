package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.FieldType;
import common.Packet;
import common.Player;
import common.PlayerData;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler.gameHandler;

public class DataTransferThread implements Runnable {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private static boolean connectionStatus = true;

    public DataTransferThread(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {

        try {
            while (true) {

                if (!connectionStatus)
                    return;

                Packet packet = receiveData();
                sendPlayerMove();
                updateMap(packet);


                // Received PlayerData test
                ArrayList<PlayerData> playerData = packet.getPlayerData();
                for (PlayerData data : playerData) {
                    System.err.println("[Nick: " + data.getNickname() + "]   " + "[Alive: " + data.isAlive() + "]   " + "[Conn: " + data.isConnected() + "]");
                }
                System.out.println();

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void stopDataTransfer(){
        connectionStatus = false;
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