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
    public Vector2d teleportTurn(Vector2d newPosition) {
        Vector2d pos = newPosition;
        Vector2d newPos = pos;
        MapDirection newOrientation = this.orientation;
        if (pos.x == -1) newPos = new Vector2d(map.width - 1, pos.y);
        else newPos = new Vector2d(0, pos.y);
        if (pos.y == -1) newOrientation.reverse().next();
        else if (pos.y == map.height - 1) newOrientation.reverse().previous();
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
            switch (direction) {
                case RIGHT:
                    orientation = orientation.next().next();
                    Vector2d newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    else if(hellExists) {
                        this.setEnergy(this.energy-map.minReproductionEnergy);
                        position = map.HellsPortal();
                    }
                    else {
                        position = this.teleportTurn(newPosition);
                    }
                    break;
                case LEFT:
                    orientation = orientation.previous().previous();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case UP:
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case DOWN:
                    orientation = orientation.reverse();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case UP_LEFT:
                    orientation = orientation.previous();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case UP_RIGHT:
                    orientation = orientation.next();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(position.add(orientation.toUnitVector()))) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case LEFT_DOWN:
                    orientation = orientation.reverse().next();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
                case RIGHT_DOWN:
                    orientation = orientation.reverse().previous();
                    newPosition = position.add(orientation.toUnitVector());
                    if (map.canMoveTo(newPosition)) {
                        this.positionChanged(this, position, newPosition);
                        position = newPosition;
                    }
                    break;
            }
        }
    }


    public void move2() { /// ?
        if (this.energy < moveEnergy) return;

        int index = this.gene;
        int direction = this.genotype[index];
        boolean hellExists = map.hellExistsMode;
        MapDirection dir = convertIdToDirection(direction);
        Vector2d newPos = this.position.add(dir.toUnitVector());
        boolean can = map.canMoveTo(newPos);

        if (hellExists && !can) {
            this.setEnergy(this.energy-map.minReproductionEnergy);
            newPos = map.HellsPortal();
        }

        else if (!hellExists && !can){
            newPos = this.teleport();
            if (newPos.y > map.high.y || newPos.y < 0) this.orientation = this.orientation.reverse();
        }

        if (can) {
            InfoField info = map.fields1.get(this.position);
            info.decrementElementsStatus();

            positionChanged(this, this.position, newPos);
            this.setPosition(newPos);

            info = map.fields1.get(this.position);
            info.incrementElementsStatus();
        }


        if (!map.predistinationMode){ // wariant "nieco szaleństwa
            int ans = (int) (Math.random() * 10);
            switch (ans) {
                case 0, 1 -> this.setRandomIndex(); // prawdopodobieństwo 20% - losowy gen
                default -> this.setNextIndex(); // prawdopodobieństo 80% - wykonuje jeden po drugim
            }
        }
        else this.setNextIndex(); // wariant "pełna predystynacja"
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
