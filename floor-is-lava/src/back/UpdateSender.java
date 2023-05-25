package back;

import common.Debug;
import common.FieldType;
import common.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateSender implements Runnable {

    private final int REFRESH_TIME = 100;

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

                for (int i = 0; i < game.playersList.size(); i++) {

                    ObjectOutputStream objectOutputStream = getPlayerOutputStream(i);
                    debug.message("Sending update");

                    FieldType[][] map = game.gameMap.getMap();
                    byte[] serializedMap = serializeCharArray(map);

                    Packet packet = new Packet(serializedMap);
                    objectOutputStream.writeObject(packet);

                    debug.message("Update has been sent");
                }

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectOutputStream getPlayerOutputStream(int i) {
        return game.playersList.get(i).getOutputStream();
    }

    public static byte[] serializeCharArray(FieldType[][] array) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(array);
        objectStream.flush();
        return byteStream.toByteArray();
    }
}
