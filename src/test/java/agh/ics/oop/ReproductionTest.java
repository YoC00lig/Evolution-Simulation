package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReproductionTest {
    @Test
    public void Test1(){ // dwa zwierzątka na jednym miejscu
        AbstractWorldMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32);
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        map.reproduction();

        assertEquals(map.livingAnimals, 3);
        assertEquals(map.listOfAnimals.size(), 3);

        int cnt = 0;
        for (Vector2d position: map.animals.keySet()){
            for (Animal animal : map.animals.get(position)){
                cnt += 1;
            }
        }

        assertEquals(cnt, 3);
    }
    @Test
    public void Test2(){ // trzy zwierzątka na jednym miejscu
        AbstractWorldMap map = new EquatorMap(5, 5, true,true,
                true, 3, 4, 5,32);
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        map.reproduction();

        assertEquals(map.livingAnimals, 4);
        assertEquals(map.listOfAnimals.size(), 4);

        int cnt = 0;
        for (Vector2d position: map.animals.keySet()){
            for (Animal animal : map.animals.get(position)){
                cnt += 1;
            }
        }

        assertEquals(cnt, 4);
    }

    @Test
    public void Test3(){ // pięc zwierzątek na jednym miejscu
        AbstractWorldMap map = new ToxicMap(5, 5, true,true,
                true,3, 4, 5,32);
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(2,3));
        map.reproduction();

        assertEquals(map.livingAnimals, 6);
        assertEquals(map.listOfAnimals.size(), 6);

        int cnt = 0;
        for (Vector2d position: map.animals.keySet()){
            for (Animal animal : map.animals.get(position)){
                cnt += 1;
            }
        }

        assertEquals(cnt, 6);
    }
}
