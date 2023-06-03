package common;

import java.util.ArrayList;
import java.util.Arrays;

public class GlobalSettings {


//      *** BACKEND ***


    // GameLoop
    public static final int REFRESH_TIME = 100;

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
    public static final int WIDTH = 115;
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
    public static final int MAXIMUM_SAFE_ZONES = 20;
    public static final ArrayList<FieldType> PLAYER_FIELDS = new ArrayList<>(Arrays.asList(FieldType.PLAYER_0, FieldType.PLAYER_1, FieldType.PLAYER_2, FieldType.PLAYER_3));
    public static final ArrayList<FieldType> RESTRICTED_FIELDS = new ArrayList<>(Arrays.asList(FieldType.SAFE_ZONE, FieldType.HOLE, FieldType.BOOST_SPEED, FieldType.BOOST_GHOST));
    public static final int MIN_SAFEZONE_SIZE = 4;
    public static final int MAX_SAFEZONE_SIZE = 8;

    // TimerThread
    public static final int TIMER_UPDATE_RATE = 1000; // 1sec
    public static final int BREAK_TIME_DURING_LAVA_TIME = 3000; // 3sec


//      *** FRONTEND ***


    // DisplayHandler
    public static final int SQUARE_SIZE = 10;

    // Server-client error flags
    public static boolean NICKNAME_ERROR_FLAG = false;
    public static boolean SERVER_FULL_FLAG = false;
}
