package agh.ics.oop;

public class Grass extends AbstractWorldMapElement {
    private final Vector2d position;
    AbstractWorldMap map;


    public Grass(Vector2d position, AbstractWorldMap map) {
        this.position = position;
        this.map = map;
    }

    public Vector2d getPosition() {
        return this.position;
    }
}
