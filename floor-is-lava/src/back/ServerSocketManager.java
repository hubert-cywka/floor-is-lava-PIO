package back;

import common.Debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager {
    private final Debug debug;
    private final boolean isDebugActivated = false;

    private final int port;
    private final Game game;


    public ServerSocketManager(int port) {
        this.port = port;
        this.game = new Game();
        this.debug = new Debug(isDebugActivated);

        launchGameLoopThread();
        launchTimerThread();
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


    private void launchGameLoopThread() {
        Thread gameLoop = new Thread(new GameLoop(game, debug));
        gameLoop.start();
    }

    private Socket acceptNewConnection(ServerSocket serverSocket) throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("New connection from IP: " + clientSocket.getInetAddress().getHostAddress());
        return clientSocket;
    }

    private void startNewClientThread(Socket clientSocket) {
        Thread clientThread = new Thread(new JoiningThread(clientSocket, game));
        clientThread.start();
    }

    private void launchTimerThread() {
        Thread timerThread = new Thread(new TimerThread(debug, game, game.playersList));
        timerThread.start();
    }
}
