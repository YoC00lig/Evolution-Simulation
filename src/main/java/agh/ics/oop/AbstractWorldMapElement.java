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
    protected void notify(Vector2d oldPos,Vector2d newPos, Animal animal){
        for (IPositionChangeObserver obs: observers) {
            obs.positionChanged(oldPos, newPos, animal);
        }
    }

}
