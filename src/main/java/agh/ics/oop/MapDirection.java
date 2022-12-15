package agh.ics.oop;

import java.util.Random;

public enum MapDirection {
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    EAST,
    WEST;

    private static final Random PRNG = new Random();
    private static final MapDirection[] directions = values();
    public static MapDirection randomDirection() {
        return directions[PRNG.nextInt(directions.length)];
    }

    public MapDirection next() {
        return switch (this) {
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case SOUTH_WEST -> SOUTH;
            case WEST -> SOUTH_WEST;
            case NORTH_WEST -> WEST;
            case NORTH -> NORTH_WEST;
            case NORTH_EAST -> NORTH;
            case EAST -> NORTH_EAST;
            case SOUTH_EAST -> EAST;
            case SOUTH -> SOUTH_EAST;
        };
    }

    public MapDirection reverse() {
        return switch (this) {
            case SOUTH -> NORTH;
            case NORTH -> SOUTH;
            case WEST -> EAST;
            case EAST -> WEST;
            case SOUTH_WEST -> NORTH_EAST;
            case SOUTH_EAST -> NORTH_WEST;
            case NORTH_EAST -> SOUTH_WEST;
            case NORTH_WEST -> SOUTH_EAST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1, 0);
            case EAST -> new Vector2d(1,0);
            case NORTH_EAST -> new Vector2d(1,1);
            case NORTH_WEST -> new Vector2d(-1, 1);
            case SOUTH_EAST -> new Vector2d(1,-1);
            case SOUTH_WEST -> new Vector2d(-1,-1);
        };
    }
    public String toString() {
        return switch (this) {
            case NORTH -> "N";
            case SOUTH -> "S";
            case WEST -> "W";
            case EAST -> "E";
            case NORTH_EAST -> "NE";
            case NORTH_WEST -> "NW";
            case SOUTH_EAST -> "SE";
            case SOUTH_WEST -> "SW";
        };
    }


}
