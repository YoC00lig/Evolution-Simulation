package agh.ics.oop;

public class Grass implements IMapElement{
    private final Vector2d position;
    AbstractWorldMap map;


    public Grass(Vector2d position, AbstractWorldMap map) {
        this.position = position;
        this.map = map;
        map.fields1.get(this.position).incrementElementsStatus();
        map.plantsNumber += 1;
        map.grasses.put(this.position, this);
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return "G " + this.position;
    }

    public String getPath(IMapElement object) {
        return "src/main/resources/grass.png";
    }
}
