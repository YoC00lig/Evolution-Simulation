package agh.ics.oop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CompareAnimalsTest {
    @Test
    public void Test1() {
        AbstractWorldMap map = new AbstractWorldMap(5, 5, true,true,
                true,true, 3, 4, 5);
        Animal a1 = new Animal(map, new Vector2d(2,3));
        Animal a2 = new Animal(map, new Vector2d(2,4));
        Animal a3 = new Animal(map, new Vector2d(1,3));
        Animal a4 = new Animal(map, new Vector2d(2,0));

        a1.setEnergy(3);
        a2.setEnergy(4);
        a3.setEnergy(3);
        a4.setEnergy(3);

        a1.setDaysOfLife(4);
        a2.setDaysOfLife(1);
        a3.setDaysOfLife(4);
        a4.setDaysOfLife(2);

        a1.setNumberOfChildren(2);
        a2.setNumberOfChildren(0);
        a3.setNumberOfChildren(1);
        a4.setNumberOfChildren(5);

        ArrayList<Animal> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);

        list.sort(Comparator.comparing(Animal::getCurrentEnergy)
                .thenComparing(Animal::getDaysOfLife)
                .thenComparing(Animal::getNumberOfChildren));

        Collections.reverse(list);

//        for (Animal animal:list){
//            System.out.println("" + animal.getCurrentEnergy() + " " + animal.getDaysOfLife() + " " + animal.getNumberOfChildren());
//        }

        assertEquals(a2, list.get(0));
        assertEquals(a1, list.get(1));
        assertEquals(a3, list.get(2));
        assertEquals(a4, list.get(3));
    }
}
