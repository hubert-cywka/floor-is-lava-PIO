package back;

import common.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static common.GlobalSettings.MAX_PLAYERS;
import static common.GlobalSettings.REFRESH_TIME;

public class GameLoop implements Runnable {

    private boolean isRunning;
    private final Game game;
    private final Debug debug;

    private Packet packet;

    public GameLoop(Game game, Debug debug) {
        this.isRunning = true;
        this.game = game;
        this.debug = debug;

        try {
            this.packet = preparePackOfData();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private synchronized void handleDataReceive(Player player) throws IOException, ClassNotFoundException {
        Thread handleReceive = new Thread(new ReceiveThread(player));
        handleReceive.start();
    }

    private synchronized void handleDataSend(Player player) throws IOException {
        debug.message("Sending update");
        ObjectOutputStream objectOutputStream = player.getOutputStream();
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

                    game.removePlayer(player.getNickname());
                    iterator.remove();
                }
            }

            if (game.playersList.isEmpty() && !game.isWaitingForPlayers()) {
                game.restartGame();
            }
        }
    }


    private Packet preparePackOfData() throws IOException {

        byte[] map = prepareMap();
        int timer = prepareTimer();
        ArrayList<PlayerData> playerData = preparePlayerData();

        return new Packet(map, timer, playerData, game.isWaitingForPlayers());
    }

    private byte[] prepareMap() throws IOException {
        FieldType[][] map = game.gameMap.getMap();
        return serializeFieldTypeArray(map);
    }

    private int prepareTimer() {
        return game.getTimer().getTimerCurrentValue();
    }

    private ArrayList<PlayerData> preparePlayerData() {

        Player player;
        ArrayList<PlayerData> playerData = new ArrayList<>(MAX_PLAYERS);

        for (int i = 0; i < MAX_PLAYERS; i++) {
            player = game.findPlayerById(i);

            if (player == null) {
                playerData.add(new PlayerData("---", false, false, -1));
                continue;
            }

            String nickname = player.getNickname();
            boolean isAlive = player.isAlive();
            int ID = player.getID();

            playerData.add(new PlayerData(nickname, isAlive, true, ID));

        }

        return playerData;
    }


    public static byte[] serializeFieldTypeArray(FieldType[][] array) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(array);
        objectStream.flush();
        return byteStream.toByteArray();
    }








    private class ReceiveThread implements Runnable{

        private Player player;

        public ReceiveThread(Player player){
            this.player = player;
        }

        @Override
        public void run() {
            ObjectInputStream objectInputStream = player.getInputStream();
            debug.message("Receiving update");

            PlayerMove playerMove;
            try {
                playerMove = (PlayerMove) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            game.movePlayer(player, playerMove.getHorizontal());
            game.movePlayer(player, playerMove.getVertical());

            try {
                packet = preparePackOfData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
