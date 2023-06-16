package back;

import common.Packet;
import common.Player;
import common.PlayerMove;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class DataReceiver implements Runnable {

    private final Player player;
    private final ObjectInputStream objectInputStream;
    private final Game game;


    public DataReceiver(Player player, Game game) {
        this.player = player;
        this.objectInputStream = player.getInputStream();
        this.game = game;
    }

    @Override
    public void run() {

        while (true){

            PlayerMove playerMove;
            try {
                playerMove = (PlayerMove) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {

                System.out.println("Failed to communicate with player " + player.getNickname());

                synchronized (this) {
                    game.removePlayer(player.getNickname());
                    game.playersList.remove(player);
                }

                return;

            }

            synchronized (this) {
                player.setMoving(false);
                game.movePlayer(player, playerMove.getHorizontal());
                game.movePlayer(player, playerMove.getVertical());
            }

        }


    }


}
