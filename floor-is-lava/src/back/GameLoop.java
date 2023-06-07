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
    ArrayList<Thread> threads;
    public static String winner;

    public GameLoop(Game game, Debug debug) {
        this.isRunning = true;
        this.game = game;
        this.debug = debug;
        this.threads = new ArrayList<>();
    }

    private synchronized void handleDataSend(Player player, Packet packet) {
        debug.message("Sending update");
        try {
            ObjectOutputStream objectOutputStream = player.getOutputStream();
            objectOutputStream.writeObject(packet);
        } catch (IOException e) {
            game.removePlayer(player.getNickname());
            game.playersList.remove(player);
        }
    }

    @Override
    public void run() {

        try {
            while (isRunning) {

                Thread.sleep(REFRESH_TIME);

                sendDataToAllPlayers();

                synchronized (this) {
                    if (game.playersList.isEmpty() && !game.isWaitingForPlayers()) {
                        game.restartGame();
                    }
                }

            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendDataToAllPlayers() throws IOException {
        Packet packet = preparePackOfData();
        for (int i = 0; i < game.playersList.size(); i++) {
            Player player = game.playersList.get(i);
            handleDataSend(player, packet);
        }
    }

    private Packet preparePackOfData() throws IOException {

        byte[] map = prepareMap();
        int timer = prepareTimer();
        ArrayList<PlayerData> playerData = preparePlayerData();

        return new Packet(map, timer, playerData, game.isWaitingForPlayers(), winner);
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
                playerData.add(new PlayerData("---", false, false, -1, 0,0,0));
                continue;
            }

            String nickname = player.getNickname();
            boolean aliveStatus = player.isAlive();
            int id = player.getID();
            int wins = player.getGamesWon();
            int speed = player.getRoundsBoostedSpeed();
            int ghost = player.getRoundsBoostedGhost();

            playerData.add(new PlayerData(nickname, aliveStatus, true, id, wins, speed, ghost));
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


}
