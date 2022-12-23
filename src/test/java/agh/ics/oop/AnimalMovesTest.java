package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalMovesTest {
    AbstractWorldMap map1 = new EquatorMap(6, 6, true, false, false, 1, 2, 20,32);
    @Test
    public void Test1() {
       Animal a = new Animal(map1, new Vector2d(5, 3));
       Animal b = new Animal(map1, new Vector2d(0, 2));
        a.setOrientation(MapDirection.EAST);
        b.setOrientation(MapDirection.WEST);
        Vector2d teleportPosition1 = a.teleportTurn(new Vector2d(6, 3), MapDirection.EAST);
        Vector2d teleportPosition2 = b.teleportTurn(new Vector2d(-1, 2), MapDirection.WEST);
       Vector2d newPosition = new Vector2d(0, 3);
       Vector2d newPosition2 = new Vector2d(5, 2);
       MapDirection newOrientation = MapDirection.EAST;
       MapDirection newOrientation2 = MapDirection.WEST;

       assertEquals(teleportPosition1, newPosition);
       assertEquals(teleportPosition2, newPosition2);
       assertEquals(newOrientation, a.getOrientation());
       assertEquals(newOrientation2, b.getOrientation());
    }

    @Test
    public void Test2() {
        Animal a = new Animal(map1, new Vector2d(3, 0));
        Animal b = new Animal(map1, new Vector2d(2, 5));
        a.setOrientation(MapDirection.NORTH);
        b.setOrientation(MapDirection.SOUTH);
        Vector2d teleportPosition1 = a.teleportTurn(new Vector2d(3, -1), MapDirection.NORTH);
        Vector2d teleportPosition2 = b.teleportTurn(new Vector2d(2, 6), MapDirection.SOUTH);
        Vector2d newPosition = new Vector2d(3, 0);
        Vector2d newPosition2 = new Vector2d(2, 5);
        MapDirection newOrientation = MapDirection.SOUTH;
        MapDirection newOrientation2 = MapDirection.NORTH;

        assertEquals(teleportPosition1, newPosition);
        assertEquals(teleportPosition2, newPosition2);
        assertEquals(newOrientation, a.getOrientation());
        assertEquals(newOrientation2, b.getOrientation());
    }

    AbstractWorldMap hellMap = new ToxicMap(6, 6, true, false, false, 1, 2, 20,32);

    @Test
    public void Test3() {
        Animal a = new Animal(hellMap, new Vector2d(3, 2));
        Animal b = new Animal(hellMap, new Vector2d(2, 4));
        a.setOrientation(MapDirection.NORTH);
        b.setOrientation(MapDirection.SOUTH);
        MapDirection aOrientation = a.getOrientation();
        a.setOrientation(aOrientation.reverse());
        MapDirection bOrientation = b.getOrientation();
        b.setOrientation(bOrientation.next());

        assertEquals(a.getOrientation(), MapDirection.SOUTH);
        assertEquals(b.getOrientation(), MapDirection.SOUTH_WEST);

        aOrientation = a.getOrientation();
        a.setOrientation(aOrientation.previous());
        bOrientation = b.getOrientation();
        b.setOrientation(bOrientation.reverse().next());

        assertEquals(a.getOrientation(), MapDirection.SOUTH_EAST);
        assertEquals(b.getOrientation(), MapDirection.EAST);
    }

}
