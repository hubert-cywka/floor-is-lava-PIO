package common;

public class GlobalSettings {


//      *** BACKEND ***


    // GameLoop
    public static final int REFRESH_TIME = 100;

    // Game
    public static final int MAX_PLAYERS = 4;

    // JoiningThread
    public static final String SERVER_HAS_FREE_SLOT = "OK";
    public static final String SERVER_IS_FULL = "FULL";
    public static final String NICKNAME_ERROR = "NICKNAME_ERROR";
    public static final String READY_TO_RECEIVE_DATA = "READY";

    // GameMap
    public static final int WIDTH = 150;
    public static final int HEIGHT = 100;
    public static final int MIN_HOLE_RADIUS = 1;
    public static final int MAX_HOLE_RADIUS = 3;
    public static final int MIN_LAVA_RADIUS = 5;
    public static final int MAX_LAVA_RADIUS = 10;
    public static final int MAP_HOLE_FREQUENCY = 300;
    public static final int BORDER_HOLE_FREQUENCY = 3;


//      *** FRONTEND ***


    // DisplayHandler
    public static final int SQUARE_SIZE = 7;

}
