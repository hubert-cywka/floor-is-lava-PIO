package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class UpdateReceiver implements Runnable{

    private final ObjectInputStream objectInputStream;

    public UpdateReceiver(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Packet packet = (Packet) objectInputStream.readObject();
                System.out.println("(Update from server) " + packet.message);
            }

        }catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
