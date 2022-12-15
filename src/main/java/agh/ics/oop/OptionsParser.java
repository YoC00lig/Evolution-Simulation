package agh.ics.oop;

public class OptionsParser {
    public static MoveDirection[] parse(int[] gens) {
        int i = 0;
        MoveDirection[] directions = new MoveDirection[gens.length];
        for (int g : gens) {
            switch(g) {
                case 0:
                    directions[i] = MoveDirection.UP;
                    i++;
                    break;
                case 1:
                    directions[i] = MoveDirection.UP_RIGHT;
                    i++;
                    break;
                case 2:
                    directions[i] = MoveDirection.RIGHT;
                    i++;
                    break;
                case 3:
                    directions[i] = MoveDirection.RIGHT_DOWN;
                    i++;
                    break;
                case 4:
                    directions[i] = MoveDirection.DOWN;
                    i++;
                    break;
                case 5:
                    directions[i] = MoveDirection.LEFT_DOWN;
                    i++;
                    break;
                case 6:
                    directions[i] = MoveDirection.LEFT;
                    i++;
                    break;
                case 7:
                    directions[i] = MoveDirection.UP_LEFT;
                    i++;
                    break;
                default:
                    throw new IllegalArgumentException(g + " is not legal move specification");
            }
        }
        return directions;
    }
}