package agh.ics.oop;
import java.util.Comparator;

public class ComparatorForEnergy implements Comparator<Animal> {
    @Override
    public int compare(Animal animal1, Animal animal2){
        return Integer.compare(animal2.getCurrentEnergy(), animal1.getCurrentEnergy());
    }
}
