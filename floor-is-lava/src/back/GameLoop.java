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
    private Iterator<Player> iterator;
    ArrayList<Thread> threads;

    public GameLoop(Game game, Debug debug) {
        this.isRunning = true;
        this.game = game;
        this.debug = debug;
        this.threads = new ArrayList<>();
    }


    private void handleDataReceive(Player player) {
        Thread recvThread = new Thread(new DataReceiver(player, game, iterator));
        recvThread.start();
        threads.add(recvThread);
    }

    private synchronized void handleDataSend(Player player, Packet packet) {
        debug.message("Sending update");
        try {
            ObjectOutputStream objectOutputStream = player.getOutputStream();
            objectOutputStream.writeObject(packet);
        } catch (IOException e) {
            game.removePlayer(player.getNickname());
            iterator.remove();
        }
    }

    @Override
    public void run() {

        try {
            while (isRunning) {

                Thread.sleep(REFRESH_TIME);

                sendDataToAllPlayers();
                receiveDataFromAllPlayers();
                waitForThreads();

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


    private Iterator<Player> prepareIterator() {
        return game.playersList.iterator();
    }

    private void sendDataToAllPlayers() throws IOException {
        iterator = prepareIterator();
        Packet packet = preparePackOfData();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            handleDataSend(player, packet);
        }
    }

    private void receiveDataFromAllPlayers() {
        iterator = prepareIterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            handleDataReceive(player);
        }
    }

    private void waitForThreads() throws InterruptedException {
        for (Thread thread : threads)
            thread.join();
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
                playerData.add(new PlayerData("---", false, false, -1, 0));
                continue;
            }

            playerData.add(new PlayerData(player.getNickname(), player.isAlive(), true, player.getID(), player.getGamesWon()));
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
