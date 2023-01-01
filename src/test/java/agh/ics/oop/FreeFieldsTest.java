package agh.ics.oop;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class FreeFieldsTest {

    @Test
    public void Test1(){ // dwa zwierzątka, każde na innym polu
        AbstractWorldMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,3));
        map.freeFields();
        int free = map.freeFields;

        assertEquals(free, 23);
        assertEquals(map.livingAnimals, 2);
    }

    @Test
    public void Test2(){ // dwa zwierzątka na tym samym polu
        AbstractWorldMap map = new EquatorMap(5, 5, true,true,
                true,3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,3));
        map.freeFields();
        int free = map.freeFields;
        assertEquals(free, 24);
        assertEquals(map.livingAnimals, 2);
    }

    @Test
    public void Test3(){ // dwa zwierzątka na tym samym polu i jedna roślina
        ToxicMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,3));
        Grass grass = new Grass(new Vector2d(2,3), map);

        map.freeFields();
        InfoField info = map.fields1.get(new Vector2d(2,3));
        int free = map.freeFields;

        assertEquals(info.elements, 3);
        assertEquals(map.livingAnimals, 2);
        assertEquals(free, 24);

        for (InfoField i: map.informations){
            if (Objects.equals(i.position, new Vector2d(2, 3)))  assertEquals(i.elements, 3);
        }
        map.freeFields();
        map.removeGrass(grass);
        assertEquals(info.elements, 2);
        assertEquals(map.livingAnimals, 2);
        assertEquals(free, 24);

        for (InfoField i: map.informations){
            if (Objects.equals(i.position, new Vector2d(2, 3)))  assertEquals(i.elements, 2);
        }

    }

    @Test
    public void Test4() { // umieszczam 3 zwierzęta, każde na innym polu
        ToxicMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,4));
        Animal animal3 = new Animal(map, new Vector2d(0,2));

        map.freeFields();

        InfoField info = map.fields1.get(new Vector2d(2,3));

        assertEquals(info.elements, 1);
        assertEquals(map.livingAnimals, 3);
        assertEquals(map.freeFields, 22);

        for (InfoField i: map.informations){
            if (Objects.equals(i.position, new Vector2d(2, 3)))  assertEquals(i.elements, 1);
        }
    }
}
