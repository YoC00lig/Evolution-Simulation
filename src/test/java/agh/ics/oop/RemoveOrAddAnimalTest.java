package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RemoveOrAddAnimalTest {
    @Test
    public void Test1(){ // zwykłe usuwanie
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,1));

        animal1.setEnergy(6);
        animal2.setEnergy(8);

        map.removeAnimal(animal1, animal1.getPosition());
        assertNull(map.animals.get(animal1.getPosition()));
        assertEquals(map.listOfAnimals.size(), 1);
    }

    @Test
    public void Test2(){ // usuwanie martwych zwierząt
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,1));

        animal1.setEnergy(0);
        animal2.setEnergy(8);

        map.removeDead();
        assertNull(map.animals.get(animal1.getPosition()));
        assertEquals(map.listOfAnimals.size(), 1);
    }

    @Test
    public void Test3(){ // zwykłe dodawanie
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,1));
        Animal animal3 = new Animal(map, new Vector2d(2,1));

        assertEquals(map.animals.get(animal1.getPosition()).size(), 1);
        assertEquals(map.animals.get(animal2.getPosition()).size(), 2);
        assertEquals(map.animals.get(animal3.getPosition()).size(), 2);
        assertEquals(map.listOfAnimals.size(), 3);
    }

    @Test
    public void Test4(){ // dodawanie przy reprodukcji
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,3));
        map.reproduction();

        assertEquals(map.animals.get(animal1.getPosition()).size(), 3);
        assertEquals(map.animals.get(animal2.getPosition()).size(), 3);
        assertEquals(map.listOfAnimals.size(), 3);
    }
}
