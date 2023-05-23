import DebugUtils.Debug;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager {

    private final int REFRESH_TIME = 250;

    private final Debug debug;
    private final boolean isDebugActivated = true;

    private ServerThread serverThread;
    private final int port;
    private final Game game;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    public ServerSocketManager(int port) {
        this.port = port;
        this.game = new Game();
        this.debug = new Debug(isDebugActivated);

        startServerThread();
        startJoinHandler();
    }


    private void startServerThread() {
        serverThread = new ServerThread();
    }

    private void startJoinHandler() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            debug.message("Server has started");
            while (true) {

                debug.message("Waiting for new connection...");
                Socket clientSocket = acceptNewConnection(serverSocket);

                debug.message("New connection accepted. Starting new client thread");
                startNewClientThread(clientSocket);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class ServerThread implements Runnable {

        public ServerThread() {}

        @Override
        public void run() {

            // Data source must be Game class!
            debug.message("Preparing informations to send to clients");
            // TODO: create method which prepare informations to send

            while (true) {

                for (int i = 0; i < game.getNumberOfPlayers(); i++) {

                    Socket playerSocket = getPlayerSocket(i);

                    debug.message("Sending informations");
                    // TODO: create method sending informations to clients
                    debug.message("Data has been sent");

                }

                try {
                    Thread.sleep(REFRESH_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }


    private Socket getPlayerSocket(int index) {
        return game.playersList.get(index).getSocket();
    }

    private Socket acceptNewConnection(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New connection from IP: " + clientSocket.getInetAddress().getHostAddress());
        return clientSocket;
    }

    private void startNewClientThread(Socket clientSocket) {
        Thread clientThread = new Thread(new ClientThread(clientSocket, game));
        clientThread.start();
    }
}
