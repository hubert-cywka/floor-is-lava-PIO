package back;

import common.Debug;

import static common.GlobalSettings.BREAK_TIME_DURING_LAVA_TIME;
import static common.GlobalSettings.TIMER_UPDATE_RATE;

public class TimerThread implements Runnable {

    private final Debug debug;
    private final Game game;


    public TimerThread(Debug debug, Game game) {
        this.debug = debug;
        this.game = game;
    }


    @Override
    public void run() {

        while (true) {

            try {

                Thread.sleep(TIMER_UPDATE_RATE);
                decrementTimer();
                handleRound();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void handleRound() throws InterruptedException {

        if (!isTimerZero())
            return;

        debug.infoMessage("IS LAVA TIME");
        fillMapWithLava();
        Thread.sleep(BREAK_TIME_DURING_LAVA_TIME);
        removeLava();
        debug.infoMessage("END OF LAVA TIME");

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

    private void decrementTimer() {
        game.getTimer().decrementTimer();
    }




}
