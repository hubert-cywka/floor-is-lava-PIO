package front.main.java.com.pio.floorislavafront.ClientSocket;

import common.Debug;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static common.GlobalSettings.READY_TO_RECEIVE_DATA;
import static common.GlobalSettings.SERVER_HAS_FREE_SLOT;
import static common.GlobalSettings.NICKNAME_ERROR_FLAG;
import static common.GlobalSettings.SERVER_FULL_FLAG;
import static front.main.java.com.pio.floorislavafront.FloorIsLavaApp.getPrimaryStage;

public class ClientApplication {
    private final String host;
    private final int port;
    private String nickname;
    private final boolean isDebugActive = true;
    private static Debug debug;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;

    public ClientApplication(String host, int port, String nickname) {
        this.port = port;
        this.host = host;
        this.debug = new Debug(isDebugActive);
        this.nickname = nickname;

        SERVER_FULL_FLAG = false;
        NICKNAME_ERROR_FLAG = false;
        connectToTheServer();
    }

    private void connectToTheServer() {
        debug.message("Establishing connection");
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        debug.message("Connected to the server");

        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            debug.message("Checking for free slot");
            if (!hasServerFreeSlot()) {
                debug.message("Server is full. Closing the connection..");
                closeIOStreams();
                socket.close();
                SERVER_FULL_FLAG = true;
                return;
            }
            debug.message("Server has free slot");

            debug.message("Sending nickname");
            sendMessageToServer(nickname);
            debug.message("Nickname has been sent");

            debug.message("Waiting for READY command");
            if (!serverIsReady()) {
                debug.errorMessage("Nickname Conflict");
                closeIOStreams();
                socket.close();
                NICKNAME_ERROR_FLAG = true;
                return;
            }
            debug.message("Server is ready");
        } catch (IOException | ClassNotFoundException e) {
            closeIOStreams();
            throw new RuntimeException(e);
        }

        startDataTransferThread();
    }

    private boolean hasServerFreeSlot() throws IOException, ClassNotFoundException {
        String status = (String) objectInputStream.readObject();
        debug.message("Server status - " + status);
        return status.equalsIgnoreCase(SERVER_HAS_FREE_SLOT);
    }

    private void sendMessageToServer(String message) throws IOException {
        objectOutputStream.writeObject(message);
    }

    private boolean serverIsReady() throws IOException, ClassNotFoundException {
        String status = (String) objectInputStream.readObject();
        return status.equalsIgnoreCase(READY_TO_RECEIVE_DATA);
    }

    private void closeIOStreams() {
        try {
            objectOutputStream.close();
            objectOutputStream.close();
        } catch (IOException ignore) {
        }
    }

    private void startDataTransferThread() {
        Thread dataTransferThread = new Thread(new DataTransferThread(objectInputStream, objectOutputStream));
        dataTransferThread.start();
    }
}
