package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Animal extends AbstractWorldMapElement{
    private int energy;
    private MapDirection orient = MapDirection.NORTH;
    private final int[] genotype;
    private Vector2d position;
    private final AbstractWorldMap map;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();

    // normal animal
    public Animal(AbstractWorldMap map, Vector2d position) {
        this.position = position;
        this.map = map;
        this.genotype = Genotype.createDNA();
        this.energy = initialEnergy;
    }
    // baby animal
    public Animal(AbstractWorldMap map, Animal parent1, Animal parent2){
        this.position = parent1.getPosition();
        this.map = map;
        this.genotype = Genotype.getChildGenotype(parent1, parent2);
        this.energy = findChildEnergy(parent1, parent2);
    }

    public static int findChildEnergy(Animal parent1, Animal parent2) {
        Animal[] parents = Genotype.getStrongerWeaker(parent1, parent2);
        return (int) (parents[0].getCurrentEnergy() * 0.75 + parents[1].getCurrentEnergy() * 0.25); // 75% energy of stronger parent
    }

    // movement
    public void move() {
        int index = (int) (Math.random() * 32);
        int direction = genotype[index];
        Vector2d unit = this.orient.toUnitVector();
        Vector2d newPos;

        switch (direction){
            case 4 -> {
                newPos =  this.position.subtract(unit);
                if (!map.canMoveTo(newPos)) {
                    newPos = teleportation(newPos);
                }
                if (map.canMoveTo(newPos)) {
                    positionChanged(this, this.position, newPos);
                    this.position = newPos;
                }
            }
            case 0 -> {
                newPos =  this.position.add(unit);
                if (!map.canMoveTo(newPos)) {
                    newPos = teleportation(newPos);
                }
                if (map.canMoveTo(newPos)) {
                    positionChanged(this, this.position, newPos);
                    this.position = newPos;
                }
            }
            default -> turn(direction);
        }
    }

    public Vector2d teleportation(Vector2d newPos) {
        Vector2d v1 = new Vector2d(map.width, 0);
        Vector2d v2 = new Vector2d(0, map.height);
        // x-coord
        if (newPos.x < 0) newPos = newPos.add(v1);
        else if (newPos.x > map.high.x) newPos = newPos.subtract(v1);
        // y-coord
        if (newPos.y < 0) newPos = newPos.add(v2);
        else if (newPos.y > map.high.y) newPos = newPos.subtract(v2);

        return newPos;
    }

    public void turn(int id) {
        for (int i = 0; i < id; i++) {
            this.orient = this.orient.next();
        }
    }

    public void reproduce(Animal partner) {
        partner.energy *= 0.75;
        this.energy *= 0.75;
    }
    // getters and setters
    public int getCurrentEnergy() {
        return this.energy;
    }
    public int[] getGenotype() {
        return this.genotype;
    }

    public void reduceEnergy() {
        this.energy -= moveEnergy;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isDead(){
        return this.energy <= 0;
    }

    // observers
    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    private void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(animal, oldPosition, newPosition);
        }
    }

}
