package back;

import common.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameLoop implements Runnable {

    private final int REFRESH_TIME = 100;

    private final Game game;
    private final Debug debug;

    public GameLoop(Game game, Debug debug) {
        this.game = game;
        this.debug = debug;
    }

    private void handleDataReceive(Player player) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = player.getInputStream();
        debug.message("Receiving update");

        PlayerMove playerMove = (PlayerMove) objectInputStream.readObject();
        game.movePlayer(player, playerMove.getHorizontal());
        game.movePlayer(player, playerMove.getVertical());
    }

    private void handleDataSend(Player player) throws IOException {
        ObjectOutputStream objectOutputStream = player.getOutputStream();
        debug.message("Sending update");

        FieldType[][] map = game.gameMap.getMap();
        byte[] serializedMap = serializeFieldTypeArray(map);

        Packet packet = new Packet(serializedMap);
        objectOutputStream.writeObject(packet);
        debug.message("Update has been sent");
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(REFRESH_TIME);

                for (int i = 0; i < game.playersList.size(); i++) {
                    Player currentPlayer = game.playersList.get(i);
                    handleDataSend(currentPlayer);
                    handleDataReceive(currentPlayer);
                }
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serializeFieldTypeArray(FieldType[][] array) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(array);
        objectStream.flush();
        return byteStream.toByteArray();
    }
}
