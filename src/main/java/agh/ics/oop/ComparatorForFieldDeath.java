package agh.ics.oop;
import java.util.Comparator;

public class ComparatorForFieldDeath implements Comparator<Vector2d>{
    @Override
    public int compare(Vector2d v1, Vector2d v2){
        return Integer.compare(v1.death, v2.death);
    }
}
