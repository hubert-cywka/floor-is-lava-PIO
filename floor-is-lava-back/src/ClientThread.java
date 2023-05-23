import DebugUtils.Debug;

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


    public ClientThread(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        this.debug = new Debug(isDebugActivated);
    }

    @Override
    public void run() {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {


            debug.message("Checking for free slot");
            if (!isFreeSlot()) {
                debug.message("Server is full");
                sendServerStatus(bufferedWriter, SERVER_IS_FULL);
                closeSocket(socket);
                return;
            }

            debug.message("Server has free slot. Sending server status to client");
            sendServerStatus(bufferedWriter, SERVER_HAS_FREE_SLOT);
            debug.message("Server status has been sent");

            debug.message("Getting basic client data");
            this.nickname = receiveNickname(bufferedReader);
            debug.message("Basic client data received: " + nickname);

            debug.message("Adding new Player to the Game");
            if (!addPlayerToTheGame()) {
                debug.errorMessage("Unable to add new player");
                debug.errorMessage("Closing the connection");
                sendServerStatus(bufferedWriter, NICKNAME_ERROR);
                closeSocket(socket);
                return;
            }

            sendServerStatus(bufferedWriter, READY_TO_RECEIVE_DATA);

            int licznik = 0;
            while (true) {

                // DZIAŁA
                String clientAction = (String) objectInputStream.readObject();
                System.out.println(clientAction + licznik);
                licznik++;

//                debug.message("Waiting for client action");
//                // TODO: create method to receive data from client
//                debug.message("Client action received");
//
//                debug.message("Client data validation process..");
//                // TODO: create method to validate client action
//                debug.message("Client Data validated properly");
//
//                // TODO: create method for checking if client wants to disconnected etc.
//
//                // TODO: create method to update data
            }


        } catch (IOException e) {
            // Connection lost
            removePlayerFromTheGame();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void closeSocket(Socket socket) throws IOException {
        socket.close();
    }

    private boolean isFreeSlot() {
        return (game.getNumberOfPlayers() < game.getMaxPlayers());
    }

    private void sendServerStatus(BufferedWriter output, String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

    private boolean addPlayerToTheGame() {
        return game.addPlayer(nickname, socket);
    }

    private void removePlayerFromTheGame() {
        game.removePlayer(nickname);
    }

    private String receiveNickname(BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine();
    }

}