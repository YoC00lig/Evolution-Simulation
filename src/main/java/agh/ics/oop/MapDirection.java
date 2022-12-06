package agh.ics.oop;

public enum MapDirection {
    SOUTH,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH,
    NORTH_EAST,
    NORTH_WEST,
    EAST,
    WEST;

    public MapDirection next() {
        return switch (this) {
            case SOUTH -> MapDirection.SOUTH_WEST;
            case SOUTH_WEST -> MapDirection.WEST;
            case WEST -> MapDirection.NORTH_WEST;
            case NORTH_WEST -> MapDirection.NORTH;
            case NORTH -> MapDirection.NORTH_EAST;
            case NORTH_EAST -> MapDirection.EAST;
            case EAST -> MapDirection.SOUTH_EAST;
            case SOUTH_EAST -> MapDirection.SOUTH;
        };
    }

    public MapDirection reverse() {
        return switch (this) {
            case SOUTH -> MapDirection.NORTH;
            case NORTH -> MapDirection.SOUTH;
            case WEST -> MapDirection.EAST;
            case EAST -> MapDirection.WEST;
            case SOUTH_WEST -> MapDirection.NORTH_EAST;
            case SOUTH_EAST -> MapDirection.NORTH_WEST;
            case NORTH_EAST -> MapDirection.SOUTH_WEST;
            case NORTH_WEST -> MapDirection.SOUTH_EAST;
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
