package back;

import common.Debug;
import common.Player;
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

    private Player player;


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
            player = addPlayerToTheGame();
            if (player == null) {
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

            while (true) {

                // Example communication

                debug.message("Waiting for client action");
                PlayerMove playerMove = getPlayerMove();
                debug.infoMessage("Move: " + playerMove);
                debug.message("Client action received");;

                debug.message("Handling move");
                game.movePlayer(player, playerMove);
                debug.message("Mave handled");

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

    private PlayerMove getPlayerMove() throws IOException, ClassNotFoundException {
        return (PlayerMove) objectInputStream.readObject();
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

    private Player addPlayerToTheGame() {
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