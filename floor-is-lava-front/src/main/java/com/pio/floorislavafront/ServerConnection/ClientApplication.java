package com.pio.floorislavafront.ServerConnection;

import com.pio.floorislavafront.ServerConnection.DebugUtils.Debug;

import java.io.*;
import java.net.Socket;

public class ClientApplication {

    private final String host;
    private final int port;
    private String nickname;

    private final boolean isDebugActive = true;
    private Debug debug;

    private Thread dataReceiver;


    public ClientApplication(String host, int port, String nickname) {
        this.port = port;
        this.host = host;
        this.debug = new Debug(isDebugActive);
        this.nickname = nickname;

        connectToTheServer();
    }

    private void connectToTheServer() {

        try (Socket socket = new Socket(host, port);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {

            debug.message("Connected to the server");

            debug.message("Checking for free slot");
            if (!hasServerFreeSlot(bufferedReader)) {
                debug.message("Server is full. Closing the connection..");
                socket.close();
                // TODO: method which displays FULL SERVER information
            }
            debug.message("Server has free slot");

            debug.message("Sending nickname");
            sendMessageToServer(bufferedWriter, nickname);
            debug.message("Nickname has been sent");

            debug.message("Waiting for READY command");
            String status = bufferedReader.readLine();
            if (status.equalsIgnoreCase("READY"))
                debug.message("READY command received");
            else {
                debug.errorMessage("Unexpected error...");
                socket.close();
            }

            dataReceiver = new Thread(new DataReceiver(objectInputStream));
            dataReceiver.start();

            while (true) {

                debug.message("Sending move");
                String testAction = "TEST_moveRight";
                objectOutputStream.writeObject(testAction);
                objectOutputStream.flush();
                debug.message("Move has been sent");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private static class DataReceiver implements Runnable {

        ObjectInputStream objectInputStream;

        public DataReceiver(ObjectInputStream objectInputStream) {
            this.objectInputStream = objectInputStream;
        }

        @Override
        public void run() {

            System.out.println("** Waiting for updated data **");
            while (true) {

                System.out.println("Receiving data");
                ExampleObject exampleObject = null;
                try {
                    exampleObject = (ExampleObject) objectInputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                System.out.println("Data received");
                System.out.println("(From Server) " + exampleObject);


            }

        }
    }


    private boolean hasServerFreeSlot(BufferedReader bufferedReader) throws IOException {

        String status = bufferedReader.readLine();
        debug.message("Server status - " + status);

        return status.equalsIgnoreCase("OK");

    }

    private void sendMessageToServer(BufferedWriter output, String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

    private void closeSocket(Socket socket) throws IOException {
        socket.close();
    }

}
