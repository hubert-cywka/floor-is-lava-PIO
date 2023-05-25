package back;

import common.Debug;
import common.PlayerMove;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final String SERVER_HAS_FREE_SLOT = "OK";
    private final String SERVER_IS_FULL = "FULL";
    private final String NICKNAME_ERROR = "NICKNAME_ERROR";
    public static final String READY_TO_RECEIVE_DATA = "READY";

    private final Debug debug;
    private final boolean isDebugActivated = true;

    private final Socket socket;
    private final Game game;
    private String nickname;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientThread(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        this.debug = new Debug(isDebugActivated);
    }

    @Override
    public void run() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            debug.message("Checking for free slot");
            if (!isFreeSlot()) {
                debug.message("Server is full");
                sendServerStatus(SERVER_IS_FULL);
                closeSocket(socket);
                return;
            }

            debug.message("Server has free slot. Sending server status to client");
            sendServerStatus(SERVER_HAS_FREE_SLOT);
            debug.message("Server status has been sent");

            debug.message("Waiting for nickname");
            nickname = receiveNickname();
            debug.message("Nickname received: " + nickname);

            debug.message("Adding new Player to the Game");
            if (!addPlayerToTheGame()) {
                debug.errorMessage("Unable to add new player");
                debug.errorMessage("Closing the connection");
                sendServerStatus(NICKNAME_ERROR);
                closeIOStreams();
                closeSocket(socket);
                return;
            }

            debug.message("Sending READY status");
            sendServerStatus(READY_TO_RECEIVE_DATA);
            debug.message("READY status has been sent");

            int i = 0;
            while (true) {

                // Example communication

                debug.message("Receiving client action");
                PlayerMove playerMove = (PlayerMove) objectInputStream.readObject();
                System.out.println(playerMove);
                i++;

                debug.message("Client action received");


//                debug.message("Waiting for client action");
//                // TODO: create method to receive data from client
//                debug.message("Client action received");
//
//                debug.message("Client data validation process..");
//                // TODO: create method to validate client action
//                debug.message("Client Data validated properly");
//
//                // TODO: create method for checking if client wants to disconnected etc.
//                // TODO: create method to update data
            }

        } catch (IOException e) {
            // Connection lost
            closeIOStreams();
            removePlayerFromTheGame();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            closeIOStreams();
            throw new RuntimeException(e);
        }
    }

    private void closeSocket(Socket socket) throws IOException {
        socket.close();
    }

    private boolean isFreeSlot() {
        return (game.getNumberOfPlayers() < game.getMaxPlayers());
    }

    private void sendServerStatus(String message) throws IOException {
        objectOutputStream.writeObject(message);
    }

    private boolean addPlayerToTheGame() {
        return game.addPlayer(nickname, objectOutputStream);
    }

    private String receiveNickname() throws IOException, ClassNotFoundException {
        return (String) objectInputStream.readObject();
    }

    private void removePlayerFromTheGame() {
        game.removePlayer(nickname);
    }

    private void closeIOStreams() {
        try {
            objectOutputStream.close();
            objectOutputStream.close();
        } catch (IOException ignore) {
        }
    }
}