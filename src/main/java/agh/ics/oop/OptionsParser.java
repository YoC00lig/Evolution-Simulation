package agh.ics.oop;

public class OptionsParser {
    public static MoveDirection[] parse(int[] gens) {

        int i = 0;
        MoveDirection[] directions = new MoveDirection[gens.length];

        for (int g : gens) {

            MoveDirection ans = switch(g) {
                case 0 -> MoveDirection.UP;
                case 1 -> MoveDirection.UP_RIGHT;
                case 2 -> MoveDirection.RIGHT;
                case 3 -> MoveDirection.RIGHT_DOWN;
                case 4 -> MoveDirection.DOWN;
                case 5 -> MoveDirection.LEFT_DOWN;
                case 6 -> MoveDirection.LEFT;
                case 7 -> MoveDirection.UP_LEFT;
                default -> throw new IllegalArgumentException(g + " is not legal move specification");
            };

            directions[i] = ans;
            i++;
        }
        return directions;
    }
}