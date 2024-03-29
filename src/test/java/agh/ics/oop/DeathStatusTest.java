package agh.ics.oop;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DeathStatusTest {
    @Test
    public void Test1(){ // dwa zwierzątka, każde na innym polu
        ToxicMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(1,3));
        animal1.setEnergy(0);
        map.removeDead();
        map.freeFields();

        assertEquals(map.livingAnimals, 1);
        assertEquals(map.freeFields, 24);
        assertEquals(map.listOfAnimals.size(), 1);

        int cnt = 0;
        for (Vector2d position: map.animals.keySet()){
            for (int i =0; i< map.animals.get(position).size(); i++) cnt +=1;
        }
        assertEquals(cnt, 1);
    }

    @Test
    public void Test2(){ // wszystkie zwierzątka umarły, a potem dodana zostaje trawa żeby sprawdzić czy wszystko się poprawnie aktualizuje
        ToxicMap map = new ToxicMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,3));
        Vector2d pos1 = animal1.getPosition();
        Vector2d pos2 = animal2.getPosition();

        animal1.setEnergy(0);
        animal2.setEnergy(0);
        map.removeDead();
        map.freeFields();

        assertEquals(map.livingAnimals, 0);
        assertEquals(map.freeFields, 25);
        assertEquals(map.deadAnimals, 2);
        assertEquals(map.fields1.get(pos1).elements, 0);
        assertEquals(map.fields1.get(pos2).elements, 0);
        assertEquals(map.fields1.get(pos1).death, 1);
        assertEquals(map.fields1.get(pos2).death, 1);

        for (InfoField i: map.informations) {
            if (Objects.equals(i.position, pos1)) {
                assertEquals(i.elements, 0);
                assertEquals(i.death, 1);
            }

            if (Objects.equals(i.position, pos2)) {
                assertEquals(i.elements, 0);
                assertEquals(i.death, 1);
            }
        }

        new Grass(pos1, map);
        assertEquals(map.fields1.get(pos1).elements, 1);
        assertEquals(map.livingAnimals, 0);
        assertEquals(map.plantsNumber, 1);
        assertEquals(map.listOfAnimals.size(), 0);

        for (InfoField i: map.informations) {
            if (Objects.equals(i.position, pos1)) assertEquals(i.elements, 1);
        }

        int cnt = 0;
        for (Vector2d position: map.animals.keySet()){
            for (int i = 0; i < map.animals.get(position).size() ; i++) cnt +=1;
        }
        assertEquals(cnt, 0);

        map.plantGrass();
        assertEquals(map.plantsNumber, 2);
    }
}
