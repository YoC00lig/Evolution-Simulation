package agh.ics.oop;

import java.util.Random;

public enum MoveDirection {
    UP,
    UP_RIGHT,
    RIGHT,
    RIGHT_DOWN,
    DOWN,
    LEFT_DOWN,
    LEFT,
    UP_LEFT;

    private static final Random PRNG = new Random();
    private static final MoveDirection[] directions = values();
    public static MoveDirection randomDirection() {
        return directions[PRNG.nextInt(directions.length)];
    }

}