package com.pio.floorislavafront.ClientSocket;

import java.io.*;
import java.net.Socket;

public class ClientApplication {

    private final String host;
    private final int port;
    private String nickname;
    private final boolean isDebugActive = true;
    private Debug debug;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    Socket socket;

    public ClientApplication(String host, int port, String nickname) {
        this.port = port;
        this.host = host;
        this.debug = new Debug(isDebugActive);
        this.nickname = nickname;

        connectToTheServer();
    }

    private void connectToTheServer() {

        debug.message("Establishing connection");
        try{
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        debug.message("Connected to the server");

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException ignore){}

        try {
            debug.message("Checking for free slot");
            if (!hasServerFreeSlot()) {
                debug.message("Server is full. Closing the connection..");
                closeIOStreams();
                socket.close();
                // TODO: method which displays FULL SERVER information
            }
            debug.message("Server has free slot");


            debug.message("Sending nickname");
            sendMessageToServer(nickname);
            debug.message("Nickname has been sent");


            debug.message("Waiting for READY command");
            if (!serverIsReady()){
                debug.errorMessage("Unexpected error...");
                socket.close();
            }
            debug.message("Server is ready");

        } catch (IOException | ClassNotFoundException e){
            closeIOStreams();
           throw new RuntimeException(e);
        }

        startUpdateReceiverThread();

        try {
            while (true) {

                Thread.sleep(3000);

                String packet = "EXAMPLE: Move_Right";
                objectOutputStream.writeObject(packet);

            }
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }


    }


    private boolean hasServerFreeSlot() throws IOException, ClassNotFoundException {
        String status = (String) objectInputStream.readObject();
        debug.message("Server status - " + status);

        return status.equalsIgnoreCase("OK");
    }

    private void sendMessageToServer(String message) throws IOException {
        objectOutputStream.writeObject(message);
    }

    private boolean serverIsReady() throws IOException, ClassNotFoundException {
        String status = (String) objectInputStream.readObject();
        return status.equalsIgnoreCase("READY");
    }

    private void closeIOStreams(){
        try{
            objectOutputStream.close();
            objectOutputStream.close();
        }catch (IOException ignore){}
    }

    private void startUpdateReceiverThread(){
        Thread updateReceiver = new Thread(new UpdateReceiver(objectInputStream));
        updateReceiver.start();
    }

}
