package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UpdateReceiver implements Runnable{

    private final ObjectInputStream objectInputStream;

    public UpdateReceiver(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {

        try {

            while (true) {

                ClientMutex.clientMutex.lock();

                Packet packet = (Packet) objectInputStream.readObject();
                System.out.println("(Update from server) " + packet.message);

                ClientMutex.clientMutex.unlock();
            }

        }catch (IOException | ClassNotFoundException e) {
            ClientMutex.clientMutex.unlock();
            throw new RuntimeException(e);
        }

    }
}
