package agh.ics.oop;
import java.util.List;
import java.util.ArrayList;

public class AbstractWorldMapElement {
    protected Vector2d position;
    protected int initialEnergy;

    public Vector2d getPosition() {
        return this.position;
    }
    protected List<IPositionChangeObserver> observers = new ArrayList<>();

}
