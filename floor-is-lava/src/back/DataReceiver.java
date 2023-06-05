package back;

import common.Player;
import common.PlayerMove;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class DataReceiver implements Runnable {

    private final Player player;
    private final ObjectInputStream objectInputStream;
    private final Game game;
    private Iterator<Player> iterator;

    public DataReceiver(Player player, Game game, Iterator<Player> iterator) {
        this.player = player;
        this.objectInputStream = player.getInputStream();
        this.game = game;
        this.iterator = iterator;
    }

    @Override
    public void run() {

        PlayerMove playerMove;
        try {
            playerMove = (PlayerMove) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Failed to communicate with player " + player.getNickname());

            synchronized (this) {
                game.removePlayer(player.getNickname());
                iterator.remove();
            }

            return;

        }

        synchronized (this) {
            game.movePlayer(player, playerMove.getHorizontal());
            game.movePlayer(player, playerMove.getVertical());
        }


    }


}
