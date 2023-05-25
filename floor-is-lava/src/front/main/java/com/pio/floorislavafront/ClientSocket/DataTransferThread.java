package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.FieldType;
import common.Packet;
import common.Player;
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
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Packet packet = (Packet) objectInputStream.readObject();
                objectOutputStream.writeObject(Player.getNextPlayerMove());
                Player.clearNextPlayerMove();

                byte[] serializedMap = packet.getMap();
                FieldType[][] map = deserializeFieldTypeArray(serializedMap);

                // Refreshing map
                Platform.runLater(() -> mapHandler(map));
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static FieldType[][] deserializeFieldTypeArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (FieldType[][]) objectStream.readObject();
    }
}
