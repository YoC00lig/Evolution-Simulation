package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EatingTest {
    @Test
    public void Test1(){ // dwa zwierzątka na jednym miejscu
        AbstractWorldMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,3));

        animal2.setEnergy(2);

        new Grass(new Vector2d(2,3), map);
        map.eat();

        assertNull(map.grasses.get(new Vector2d(2, 3)));
        assertEquals(map.grasses.size(), 0);
        assertEquals(animal1.getCurrentEnergy(), map.initialEnergy + map.plantEnergy);
        assertEquals(animal2.getCurrentEnergy(), 2); // drugie zwierzątko bylo slabsze więc je tylko mocniejsze
    }

    @Test
    public void Test2(){ // dwa zwierzątka na jednym miejscu
        AbstractWorldMap map = new EquatorMap(5, 5, true,false,
                true,3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,3));

        animal2.setEnergy(2);

        new Grass(new Vector2d(2,3), map);
        map.eat();

        assertNull(map.grasses.get(new Vector2d(2, 3)));
        assertEquals(map.grasses.size(), 0);
        assertEquals(animal1.getCurrentEnergy(), map.initialEnergy + map.plantEnergy);
        assertEquals(animal2.getCurrentEnergy(), 2); // drugie zwierzątko bylo slabsze więc je tylko mocniejsze
    }

}
