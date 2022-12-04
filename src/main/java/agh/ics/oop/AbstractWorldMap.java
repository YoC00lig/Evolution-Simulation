package agh.ics.oop;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
public abstract class AbstractWorldMap implements IPositionChangeObserver{
    protected Vector2d low;
    protected Vector2d high;
    protected int width;
    protected int height;
    protected Vector2d junglelow;
    protected Vector2d junglehigh;
    protected int minReproductionEnergy;
    protected LinkedHashMap<Vector2d, LinkedList<Animal>> animals = new LinkedHashMap<>();
    protected ArrayList<Animal> listOfAnimals = new ArrayList<>();
    protected LinkedHashMap<Vector2d, Grass> grasses = new LinkedHashMap<>();

    // positions checking etc
    public boolean canMoveTo(Vector2d position) {
        return position.follows(low) && position.precedes(high);
    }

    public boolean canMoveToJungle(Vector2d position) {
        return position.follows(junglelow) && position.precedes(junglehigh);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.removeAnimal(animal, oldPosition);
        this.addAnimal(animal, newPosition);
    }


    public void place(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (!(pos.x >= this.width || pos.x < 0 || pos.y >= this.height || pos.y < 0)){
            this.addAnimal(animal, pos);
            animal.addObserver(this);
            listOfAnimals.add(animal);
        }
    }

    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (animals.get(position) == null) {
            return grasses.get(position);
        }
        return animals.get(position).get(0);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    // animal general functions
    public void moveAllAnimals() {
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) {
                animal.move();
                animal.reduceEnergy();
            }
        }
    }

    public void addAnimal(Animal animal, Vector2d newPosition){
        if (animals.get(newPosition) == null) {
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animals.put(newPosition, list);
        }
        else animals.get(newPosition).add(animal);
    }

    public void removeAnimal(Animal animal, Vector2d oldPosition) {
        LinkedList<Animal> list =  animals.get(oldPosition);
        if (list != null) {
            list.remove(animal);
            if (list.size() == 0) animals.remove(oldPosition);
        }
    }

    public void removeDead() {
        for (Vector2d position: animals.keySet()){
            for (Animal animal: animals.get(position)){
                if (animal.isDead()) removeAnimal(animal, position);
            }
        }
    }

    // grass operations
    public void plantGrassInSteppe(int numberOfGrassFields) {
        Random random = new Random();
        int x, y;
        Vector2d position;
        for (int i = 0; i < numberOfGrassFields; i++){
            do {
                x = random.nextInt(this.high.x + 1);
                y = random.nextInt(this.high.y + 1);
                position = new Vector2d(x, y);
            } while (isOccupied(position) || canMoveToJungle(position));
            Grass grassElement = new Grass(position, this);
            grasses.put(position, grassElement);
        }
    }

    public void plantGrassInJungle(int numberOfGrassFields) {
        Random random = new Random();
        int x, y;
        Vector2d position;
        for (int i = 0; i < numberOfGrassFields; i++){
            do {
                x = junglelow.x + random.nextInt(junglehigh.x - junglelow.x + 1);
                y = junglelow.y + random.nextInt(junglehigh.y - junglelow.y + 1);
                position = new Vector2d(x, y);
            } while (isOccupied(position));
            Grass grassElement = new Grass(position, this);
            grasses.put(position, grassElement);
        }
    }

    public void removeGrass(Grass grass) {
        grasses.remove(grass.getPosition(), grass);
    }

    // reproduction

    public List<Animal> getParents(Vector2d position){
        List<Animal> parents = animals.get(position);
        parents.sort(new ComparatorForEnergy());
        return parents.subList(0,2);
    }

    public Animal getBaby(List<Animal> parents){
        Animal parent1 = parents.get(0);
        Animal parent2 = parents.get(1);
        return new Animal(this, parent1, parent2);
    }

    public void reproduction() {
        for (Vector2d position : animals.keySet()){
            if (animals.get(position).size() >= 2) {

                List<Animal> parents = getParents(position);
                Animal stronger = Genotype.getStrongerWeaker(parents.get(0), parents.get(1))[0];
                Animal weaker = Genotype.getStrongerWeaker(parents.get(0), parents.get(1))[1];

                if (weaker.getCurrentEnergy() >= minReproductionEnergy) {
                    Animal baby = getBaby(parents);
                    stronger.reproduce(weaker);
                    this.place(baby);
                }
            }
        }
    }

    // getters and setters
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public Vector2d getLow() {
        return this.low;
    }
    public Vector2d getHigh() {
        return this.high;
    }
    public Vector2d getJungleLow() {
        return this.junglelow;
    }
    public Vector2d getJungleHigh() {
        return this.junglehigh;
    }
}
