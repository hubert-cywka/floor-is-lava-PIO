package back;

import common.Debug;
import common.Packet;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateSender implements Runnable {

    private final int REFRESH_TIME = 8000;

    private final Game game;
    private final Debug debug;


    public UpdateSender(Game game, Debug debug) {
        this.game = game;
        this.debug = debug;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(REFRESH_TIME);

                ServerMutex.serverMutex.lock();
                for (int i = 0; i < game.playersList.size(); i++) {
                    ObjectOutputStream objectOutputStream = getPlayerOutputStream(i);
                    debug.message("Sending update");
                    objectOutputStream.writeObject(new Packet("new update to client"));
                    debug.message("Update has been sent");
                }
                ServerMutex.serverMutex.unlock();
            }
        } catch (IOException | InterruptedException e) {
            ServerMutex.serverMutex.unlock();
            throw new RuntimeException(e);
        }
    }

    private ObjectOutputStream getPlayerOutputStream(int i) {
        return game.playersList.get(i).getOutputStream();
    }
}
