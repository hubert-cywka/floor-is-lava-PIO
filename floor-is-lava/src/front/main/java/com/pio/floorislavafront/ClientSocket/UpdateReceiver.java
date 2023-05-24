package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.Packet;
import javafx.application.Platform;

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

                // Printing map to console
//                packet.printMap();

                // Refreshing map
                Platform.runLater(() -> mapHandler(packet.map));

                System.out.println("(Update from server) " + packet.message);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
