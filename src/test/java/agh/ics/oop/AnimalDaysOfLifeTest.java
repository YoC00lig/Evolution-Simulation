package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalDaysOfLifeTest {

    @Test
    public void Test1(){ // sprawdza czy wartości są dobrze inicjalizowane, zwierzątko domyślnie gdy zostaje tworzone zyje jeden dzień
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,3));

        assertEquals(animal1.daysOfLife, 1);
        assertEquals(animal2.daysOfLife, 1);
    }

    @Test
    public void Test2(){ // sprawdza, czy informacje dobrze się aktualizują
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,3));

        animal1.aliveNextDay();
        assertEquals(animal1.daysOfLife, 2);

        for (int i = 0; i < 5; i++) {
            animal2.aliveNextDay();
        }
        assertEquals(animal2.daysOfLife, 6);

    }

    @Test
    public void Test3(){ // sprawdza, czy informacje dobrze aktualizują się w listach na mapie
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,true,true, 3, 4, 5);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(1,3));

        animal1.aliveNextDay();
        for (int i = 0; i < 5; i++) {
            animal2.aliveNextDay();
        }

        for (Animal animal: map.listOfAnimals) {
            if (animal == animal1) assertEquals(animal.daysOfLife, 2);
            else if (animal != null) assertEquals(animal.daysOfLife, 6);
        }

        for (Vector2d position: map.animals.keySet()){
            for (Animal animal: map.animals.get(position)){
                if (animal == animal1) assertEquals(animal.daysOfLife, 2);
                else if (animal != null) assertEquals(animal.daysOfLife, 6);
            }
        }
    }

}
