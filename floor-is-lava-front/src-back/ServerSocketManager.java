import DebugUtils.Debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager {

    private final Debug debug;
    private final boolean isDebugActivated = true;

    private final int port;
    private final Game game;


    public ServerSocketManager(int port) {
        this.port = port;
        this.game = new Game();
        this.debug = new Debug(isDebugActivated);

        launchUpdateSenderThread();
        startJoinHandler();
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


    private void launchUpdateSenderThread(){
        Thread updateSender = new Thread(new UpdateSender(game, debug));
        updateSender.start();
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
