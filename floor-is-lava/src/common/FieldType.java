package common;

import java.io.Serializable;

public enum FieldType implements Serializable {
    PLAYER_0,
    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    WALL,
    BORDER,
    SAFE_ZONE,
    FLOOR,
    LAVA,
    BOOST_SPEED,
    BOOST_GHOST,
    HOLE;
}
