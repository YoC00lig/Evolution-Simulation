package agh.ics.oop;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Animal extends AbstractWorldMapElement{
    private int energy;
    private MapDirection orientation;
    private final int[] genotype;
    private final int genotypeLength;
    private MoveDirection[] directions;
    private Vector2d position;
    private final AbstractWorldMap map;
    protected int gene, daysOfLife, numberOfChildren, isDead, eatenPlants;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();


    // normal animal
    public Animal(AbstractWorldMap map, Vector2d position) {
        this.position = position;
        this.map = map;
        this.genotype = Genotype.createDNA();
        this.genotypeLength = genotype.length;
        this.directions = OptionsParser.parse(genotype);
        this.initialEnergy = map.initialEnergy;
        this.energy = initialEnergy;
        this.orientation = MapDirection.randomDirection();
        this.gene = (int) (Math.random() * genotypeLength); // indeks w tablicy genotypów, który będzie wskazywać na następny ruch - gdy tworzymy zwierzatko to jest ustawiany randomowo
        this.daysOfLife = 1;
        this.numberOfChildren = 0;
        this.isDead = 0; // 0 oznacza, że jeszcze żyje. Każda inna liczba oznacza którego dnia zmarło
        this.eatenPlants = 0; // tyle roslinek zjadlo
        map.livingAnimals += 1;
        map.place(this);
    }
    // baby animal
    public Animal(AbstractWorldMap map, Animal parent1, Animal parent2){
        this.position = parent1.getPosition();
        this.map = map;
        this.genotype = Genotype.getChildGenotype(parent1, parent2, map.isCrazyMode);
        this.genotypeLength = genotype.length;
        this.directions = OptionsParser.parse(genotype);
        this.energy = findChildEnergy(parent1, parent2);
        this.orientation = MapDirection.randomDirection();
        this.gene = (int) (Math.random() * genotypeLength);
        this.daysOfLife = 1;
        this.moveEnergy = map.moveEnergy;
        this.isDead = 0;
        this.numberOfChildren = 0;
        this.eatenPlants = 0;
        map.livingAnimals += 1;
    }

    public int findDominantGenotype() {
        int[] cnt = new int[8];
        for (int gen: this.genotype) {
            cnt[gen] += 1;
        }
        int maxi = 0;
        int gen = 0;
        for (int i = 0; i < 8; i++) {
            if (cnt[i] > maxi) {
                gen = i;
                maxi = cnt[i];
            }
        }
        return gen;
    }

    public static int findChildEnergy(Animal parent1, Animal parent2) {
        Animal[] parents = Genotype.getStrongerWeaker(parent1, parent2);
        return (int) (parents[0].getCurrentEnergy() * 0.75 + parents[1].getCurrentEnergy() * 0.25);
    }
/// poruszanie
    public Vector2d teleportTurn(Vector2d newPosition, MapDirection newOrientation) {

        MapDirection newOrient = newOrientation;
        Vector2d newPos = newPosition;

        if (newPos.x == -1) newPos = new Vector2d(map.width - 1, newPos.y);
        else newPos = new Vector2d(0, newPos.y);
        if (newPos.y == -1) newOrient.reverse();
        else if (newPos.y == map.height - 1) newOrientation.reverse();
        orientation = newOrientation;

        return newPos;
    }

    public void setNextIndex() {
        this.gene = this.gene + 1;
        if (this.gene == genotypeLength) this.gene = 0;
    }

    public void setRandomIndex() {
        this.gene = (int) (Math.random() * genotypeLength);
    }


    public void move() {
        chooseGene();
        MoveDirection direction = directions[gene];
        boolean hellExists = map.hellExistsMode;
        if (direction != null) {
            MapDirection newOrientation = orientation;
            switch (direction) {
                case RIGHT:
                    newOrientation = orientation.next().next();
                    break;
                case LEFT:
                    newOrientation = orientation.previous().previous();
                    break;
                case UP:
                    newOrientation = orientation;
                    break;
                case DOWN:
                    newOrientation = orientation.reverse();
                    break;
                case UP_LEFT:
                    newOrientation = orientation.previous();
                    break;
                case UP_RIGHT:
                    newOrientation = orientation.next();
                    break;
                case LEFT_DOWN:
                    newOrientation = orientation.reverse().next();
                    break;
                case RIGHT_DOWN:
                    newOrientation = orientation.reverse().previous();
                    break;
            }
            Vector2d newPosition = position.add(newOrientation.toUnitVector());
            if (map.canMoveTo(newPosition)) {
                orientation = newOrientation;
            }
            else if(hellExists) {
                this.setEnergy(this.energy-map.minReproductionEnergy);
                newPosition = map.HellsPortal();
                orientation = newOrientation;
            }
            else {
                newPosition = this.teleportTurn(newPosition, newOrientation);
            }
            InfoField info = map.fields1.get(this.position);
            info.decrementElementsStatus();
            positionChanged(this, this.position, newPosition);
            position = newPosition;
            info = map.fields1.get(this.position);
            info.incrementElementsStatus();
        }
    }


    public void chooseGene() {
        if (!map.predistinationMode){ // wariant "nieco szaleństwa
            int ans = (int) (Math.random() * 10);
            switch (ans) {
                case 0, 1 -> this.setRandomIndex(); // prawdopodobieństwo 20% - losowy gen
                default -> this.setNextIndex(); // prawdopodobieństo 80% - wykonuje jeden po drugim
            }
        }
        else this.setNextIndex(); // wariant "pełna predystynacja"
    }

// poruszanie
    public void reproduce(Animal partner) {
        partner.energy *= 0.75;
        this.energy *= 0.25;
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

    public void aliveNextDay() { // to change
        this.daysOfLife += 1;
    }
    public void setEnergy(int value) {
        LinkedList<Animal> list = map.animals.get(this.position);
        LinkedList<Animal> toUpdate = new LinkedList<>();
        for (Animal animal: list){
            if (animal != this) toUpdate.add(animal);
            else {
                this.energy = value;
                toUpdate.add(this);
            }
        }
        map.rewrite(toUpdate);
    }
    public void setPosition(Vector2d pos) {this.position = pos;}
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

    public MapDirection convertIdToDirection(int directionId) {
        return switch (directionId) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTH_EAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTH_EAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTH_WEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + directionId);
        };

    }

    public void addNewChild() {
        this.numberOfChildren += 1;
    }

    public void atePlant() {
        this.eatenPlants += 1;
    }

    public String toString() {
        return "A " + this.position;
    }
}
