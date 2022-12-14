package agh.ics.oop;

public class Grass extends AbstractWorldMapElement {
    private final Vector2d position;
    AbstractWorldMap map;


    public Grass(Vector2d position, AbstractWorldMap map) {
        this.position = position;
        this.map = map;
        map.fields1.get(this.position).incrementElementsStatus();
        map.plantsNumber += 1;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return "G " + this.position;
    }
}
