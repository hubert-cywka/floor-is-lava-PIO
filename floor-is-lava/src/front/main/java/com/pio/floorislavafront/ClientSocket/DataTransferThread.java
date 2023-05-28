package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.FieldType;
import common.Packet;
import common.Player;
import front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler.mapHandler;

public class DataTransferThread implements Runnable {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public DataTransferThread(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        DisplayHandler.initTextures();
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

                // method using only in program testing
                printTimer(packet);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            objectOutputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    private void printTimer(Packet packet) {
        System.out.println("Timer: " + packet.getTimer());
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
        Platform.runLater(() -> mapHandler(map));
    }
}
