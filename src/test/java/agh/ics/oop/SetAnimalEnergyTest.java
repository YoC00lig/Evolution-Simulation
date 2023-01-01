package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SetAnimalEnergyTest {

    @Test
    public void Test1(){ // prosty test na ustawianie energii
        AbstractWorldMap map = new ToxicMap(5, 5, true,true,
                true,3, 4, 5,32,3,6,2);
        Animal animal1 = new Animal(map, new Vector2d(2,3));
        Animal animal2 = new Animal(map, new Vector2d(2,1));

        animal1.setEnergy(6);
        animal2.setEnergy(8);

        assertEquals(animal1.getCurrentEnergy(), 6);
        assertEquals(animal2.getCurrentEnergy(), 8);
    }


    @Test
    public void Test2(){ // sprawdzanie, czy wartości aktualizują się w obydwu listach
        AbstractWorldMap map = new EquatorMap(5, 5, true,true,
                true, 3, 4, 5,32,3,6,2);
        new Animal(map, new Vector2d(2,3));
        new Animal(map, new Vector2d(3,1));

        for (Animal animal: map.listOfAnimals) {
            if (animal != null) {
                animal.setEnergy(10);
            }
        }

        for (Animal animal: map.listOfAnimals) {
            assertEquals(animal.getCurrentEnergy(), 10);
        }

        for (Vector2d position: map.animals.keySet()){
            for (Animal animal: map.animals.get(position)){
                if (animal != null) assertEquals(animal.getCurrentEnergy(), 10);
            }
        }
    }

}
