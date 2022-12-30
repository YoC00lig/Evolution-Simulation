package agh.ics.oop;
import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{
    // trial commit
    private int energy;
    private MapDirection orientation;
    private final int[] genotype;
    private final int genotypeLength;
    private MoveDirection[] directions;
    private Vector2d position;
    private int initialEnergy;
    private final AbstractWorldMap map;
    protected int gene, daysOfLife, numberOfChildren, isDead, eatenPlants;
    protected List<IPositionChangeObserver> observers = new ArrayList<>();


    // normal animal
    public Animal(AbstractWorldMap map, Vector2d position) {
        this.position = position;
        this.map = map;
        this.genotype = Genotype.createDNA(map.numberOfGenes);
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
    }
    // baby animal
    public Animal(AbstractWorldMap map, Animal parent1, Animal parent2){
        this.position = parent1.getPosition();
        this.map = map;
        this.genotype = Genotype.getChildGenotype(parent1, parent2, map.isCrazyMode, map.numberOfGenes, map);
        this.genotypeLength = genotype.length;
        this.directions = OptionsParser.parse(genotype);
        this.energy = findChildEnergy(parent1, parent2);
        this.orientation = MapDirection.randomDirection();
        this.gene = (int) (Math.random() * genotypeLength);
        this.daysOfLife = 1;
        this.isDead = 0;
        this.numberOfChildren = 0;
        this.eatenPlants = 0;
        map.livingAnimals += 1;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public int findDominantGenotype() {
        int[] cnt = new int[8];
        for (int gen: this.genotype) cnt[gen] += 1;
        int Gen = 0;
        for (int i = 0; i < cnt.length; i++) {
            Gen = cnt[i] > cnt[Gen] ? i : Gen;
        }
        return Gen;
    }

    public static int findChildEnergy(Animal parent1, Animal parent2) {
        return (int) (parent1.getCurrentEnergy() * 0.75 + parent2.getCurrentEnergy() * 0.25);
    }
    /// poruszanie
    public Vector2d teleportTurn(Vector2d newPosition, MapDirection newOrientation) {
        Vector2d newPos = newPosition;

        if (newPos.x == -1){
            newPos = new Vector2d(map.width - 1, newPos.y);
        }
        else if (newPos.x == map.width) {
            newPos = new Vector2d(0, newPos.y);
        }
        if (newPos.y == -1) {
            newPos = new Vector2d(newPos.x, 0);
            newOrientation = newOrientation.reverse();
        }
        else if (newPos.y == map.height) {
            newPos = new Vector2d(newPos.x, map.height - 1);
            newOrientation = newOrientation.reverse();
        }
        this.orientation = newOrientation;

        return newPos;
    }

    public void setNextIndex() {
        this.gene = this.gene + 1;
        if (this.gene == genotypeLength) this.gene = 0;
    }

    public void setRandomIndex() {
        this.gene = (int) (Math.random() * genotypeLength);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }


    public void move() {
        chooseGene();
        MoveDirection direction = directions[gene];
        boolean hellExists = map.hellExistsMode;
        if (direction != null) {
            MapDirection newOrientation = switch (direction) {
                case RIGHT -> orientation.next().next();
                case LEFT -> orientation.previous().previous();
                case UP -> orientation;
                case DOWN -> orientation.reverse();
                case UP_LEFT -> orientation.previous();
                case UP_RIGHT -> orientation.next();
                case LEFT_DOWN -> orientation.reverse().next();
                case RIGHT_DOWN -> orientation.reverse().previous();
            };

            Vector2d newPosition = position.add(newOrientation.toUnitVector());
            if (map.canMoveTo(newPosition)) orientation = newOrientation;
            else if(hellExists) {
                this.setEnergy(this.energy-map.minReproductionEnergy);
                newPosition = map.HellsPortal();
                orientation = newOrientation;
            }
            else newPosition = this.teleportTurn(newPosition, newOrientation);

            InfoField info = map.fields1.get(this.position);
            info.decrementElementsStatus();
            positionChanged(this.position, newPosition);
            position = newPosition;
            info = map.fields1.get(this.position);
            info.incrementElementsStatus();
            this.energy -= 1;
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

    public void aliveNextDay() {
        this.daysOfLife += 1;
    }
    public void setEnergy(int value) {
        this.energy = value;
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

    public void addNewChild() {
        this.numberOfChildren += 1;
    }

    public void atePlant() {
        this.eatenPlants += 1;
    }

    public String toString() {
        return "A " + this.position;
    }

    public String getPath(IMapElement object) {
        if (this.hasDominantGenotype()) return "src/main/resources/snail2.png";
        else return "src/main/resources/snail.png";
    }

    public int getDaysOfLife() {return this.daysOfLife;}
    public int getNumberOfChildren(){return this.numberOfChildren;}

    public void setDaysOfLife(int value){this.daysOfLife = value;}
    public void setNumberOfChildren(int value){ this.numberOfChildren = value;}
    public int getActiveGen() {
        return this.genotype[this.gene];
    }
    public int getEatenPlants() {
        return this.eatenPlants;
    }
    public boolean isAlive() {
        return this.isDead == 0;
    }
    public boolean hasDominantGenotype() {
        Statistics stats = new Statistics(map);
        return this.findDominantGenotype() == stats.findDominantGenotype();
    }
}