package back;

import common.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

public class GameLoop implements Runnable {

    private final int REFRESH_TIME = 100;

    private boolean isRunning;
    private final Game game;
    private final Debug debug;

    public GameLoop(Game game, Debug debug) {
        this.isRunning = true;
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
        debug.message("Sending update");
        ObjectOutputStream objectOutputStream = player.getOutputStream();
        Packet packet = preparePackOfData();
        objectOutputStream.writeObject(packet);

    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(REFRESH_TIME);
            } catch (InterruptedException e) {
                debug.errorMessage(e.getMessage());
            }

            Iterator<Player> iterator = game.playersList.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();

                try {
                    handleDataSend(player);
                    handleDataReceive(player);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Failed to communicate with player " + player.getNickname());
                    debug.errorMessage(e.getMessage());
                    iterator.remove();
                }
            }
        }
    }

    public static byte[] serializeFieldTypeArray(FieldType[][] array) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(array);
        objectStream.flush();
        return byteStream.toByteArray();
    }

    private Packet preparePackOfData() throws IOException {
        FieldType[][] map = game.gameMap.getMap();
        byte[] serializedMap = serializeFieldTypeArray(map);

        int timer = game.getTimer().getTimerCurrentValue();


        return new Packet(serializedMap, timer);
    }
}
