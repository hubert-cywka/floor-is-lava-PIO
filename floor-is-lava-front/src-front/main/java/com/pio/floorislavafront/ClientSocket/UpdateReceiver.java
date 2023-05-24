package com.pio.floorislavafront.ClientSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

class Packet implements Serializable {

    public String message;

    public Packet(String message){
        this.message = message;
    }

}

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
