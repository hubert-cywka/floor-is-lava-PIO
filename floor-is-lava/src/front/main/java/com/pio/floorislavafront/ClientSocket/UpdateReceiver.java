package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.FieldType;
import common.Packet;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static front.main.java.com.pio.floorislavafront.DisplayUtils.DisplayHandler.mapHandler;

public class UpdateReceiver implements Runnable {

    private final ObjectInputStream objectInputStream;

    public UpdateReceiver(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Packet packet = (Packet) objectInputStream.readObject();

                byte[] serializedMap = packet.getMap();
                FieldType[][] map = deserializeCharArray(serializedMap);

                // Refreshing map
                Platform.runLater(() -> mapHandler(map));
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static FieldType[][] deserializeCharArray(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (FieldType[][]) objectStream.readObject();
    }
}
