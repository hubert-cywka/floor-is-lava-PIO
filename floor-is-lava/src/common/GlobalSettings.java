package common;

public class GlobalSettings {


//      *** BACKEND ***


    // GameLoop
    public static final int REFRESH_TIME = 1000;

    // Game
    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    public static final int ROUND_TIME = 5;

    // JoiningThread
    public static final String SERVER_HAS_FREE_SLOT = "OK";
    public static final String SERVER_IS_FULL = "FULL";
    public static final String NICKNAME_ERROR = "NICKNAME_ERROR";
    public static final String READY_TO_RECEIVE_DATA = "READY";

    // GameMap
    public static final int WIDTH = 120;
    public static final int HEIGHT = 75;
    public static final int MIN_HOLE_RADIUS = 4;
    public static final int MAX_HOLE_RADIUS = 8;
    public static final int MIN_LAVA_RADIUS = 5;
    public static final int MAX_LAVA_RADIUS = 10;
    public static final int MAP_HOLE_FREQUENCY = 500;
    public static final int BORDER_HOLE_FREQUENCY = 3;
    public static final int ROUNDS_TO_INCREASE_LAVA_SIZE = 3;
    public static final int ROUNDS_TO_DECREASE_SAFE_ZONES_COUNT = 5;
    public static final int MINIMUM_SAFE_ZONES = 2;
    public static final int MAXIMUM_SAFE_ZONES = 5;

    // TimerThread
    public static final int TIMER_UPDATE_RATE = 1000; // 1sec
    public static final int BREAK_TIME_DURING_LAVA_TIME = 3000; // 3sec


//      *** FRONTEND ***


    // DisplayHandler
    public static final int SQUARE_SIZE = 10;

}
