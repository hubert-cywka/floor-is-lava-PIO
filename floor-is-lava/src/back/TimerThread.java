package back;

import common.Debug;

public class TimerThread implements Runnable {

    private final int TIMER_UPDATE_RATE = 1000; // 1sec

    private final Debug debug;
    private final Game game;


    public TimerThread(Debug debug, Game game) {
        this.debug = debug;
        this.game = game;
    }


    @Override
    public void run() {

        debug.message("Timer thread has started");
        while (true) {

            try {

                Thread.sleep(TIMER_UPDATE_RATE);
                decrementTimer();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void decrementTimer() {
        game.getTimer().decrementTimer();
    }
}
