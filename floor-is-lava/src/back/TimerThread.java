package back;

import common.Debug;
import common.FieldType;
import common.Player;
import common.PowerUp;

import java.util.ArrayList;
import java.util.List;

import static back.Game.getRandomNumberInRange;
import static common.GlobalSettings.BREAK_TIME_DURING_LAVA_TIME;
import static common.GlobalSettings.TIMER_UPDATE_RATE;

public class TimerThread implements Runnable {

    private final Debug debug;
    private final Game game;
    private int floodStage;


    public TimerThread(Debug debug, Game game) {
        this.debug = debug;
        this.game = game;
        this.floodStage = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(4000);
                if (!game.isWaitingForPlayers()) {
                    decrementTimer();
                    handleRound();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void handleRound() throws InterruptedException {

        if (!isTimerZero()) {
            floodStage += 1;
            game.gameMap.generateLavaBorders(floodStage);
            return;
        } else {
            decrementPowerUpRound(game.playersList);
            generatePowerUps(4);
            floodStage = 0;
        }

        debug.infoMessage("IS LAVA TIME");
        fillMapWithLava();
        killPlayersInLava();
        Thread.sleep(BREAK_TIME_DURING_LAVA_TIME);
        removeLava();
//        updatePlayerLastStandingField();
        debug.infoMessage("END OF LAVA TIME");

        if (!game.playersList.isEmpty()) {
            game.incrementRound();
        } else {
            game.resetRound();
        }
    }

    private void killPlayersInLava(){

        for(Player player : game.playersList){
            if (player.getLastStandingField() != FieldType.SAFE_ZONE){

                if (!(player.getLastStandingField() == FieldType.HOLE))
                    player.setLastStandingField(FieldType.LAVA);

                game.killPlayer(player);
            }
        }

    }

    private void removeLava(){
        game.gameMap.setSafeTime();
    }

    private void fillMapWithLava(){
        game.gameMap.setLavaTime();
    }

    private boolean isTimerZero(){
        return game.getTimer().getTimerCurrentValue() == 0;
    }

    private void updatePlayerLastStandingField(){
        for (Player player : game.playersList)
            player.setLastStandingField(FieldType.FLOOR);
    }

    private void decrementTimer() {
        game.getTimer().decrementTimer();
    }

    private void generatePowerUps(int number){
        int powerUps=0;
        while(powerUps < number){
            int powerType = getRandomNumberInRange(0,1);
            if(powerType==1)
                game.addPowerUpOnMap(new PowerUp(FieldType.BOOST_SPEED, game.findValidPositionOnMap()));
            else
                game.addPowerUpOnMap(new PowerUp(FieldType.BOOST_GHOST, game.findValidPositionOnMap()));
            powerUps++;
        }

    }

    private void decrementPowerUpRound(List<Player> playerList){
        for(Player player : playerList)
            player.decrementPowerUpRound();
    }
}
