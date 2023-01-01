package agh.ics.oop;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

abstract public class AbstractWorldMap implements IPositionChangeObserver{
    public Vector2d low, high;
    public int day, averageEnergy, averageLifeLength, freeFields, numberOfGenes;
    protected int width, height, minReproductionEnergy, plantEnergy, initialEnergy;

    public ConcurrentHashMap<Vector2d, LinkedList<Animal>> animals = new ConcurrentHashMap<>();
    public ArrayList<Animal> listOfAnimals = new ArrayList<>();
    public ConcurrentHashMap<Vector2d, Grass> grasses = new ConcurrentHashMap<>();
    protected LinkedHashMap<Vector2d, InfoField> fields1; // łatwo można odwoływać się po kluczu do informacji, nie trzeba przeszukiwać całej ArrayList
    protected final boolean predistinationMode, isCrazyMode, hellExistsMode;
    //for statistics
    int deadAnimals = 0;
    int daysOfLifeDeadsSum = 0;
    int livingAnimals = 0;
    int plantsNumber = 0;
    int dominantGenotype = 0;


    public AbstractWorldMap(int width, int height,boolean predistination, boolean isCrazyMode, boolean hellExistsMode, int reproductionE, int plantE, int initialE, int numberOfGenes) {
        this.width = width;
        this.height = height;
        this.low = new Vector2d(0,0);
        this.high = new Vector2d(this.width-1,this.height-1);
        this.day = 1;
        this.freeFields = this.width * this.height;
        this.predistinationMode = predistination;
        this.isCrazyMode = isCrazyMode;
        this.hellExistsMode = hellExistsMode;
        this.plantEnergy = plantE;
        this.minReproductionEnergy = reproductionE;
        this.initialEnergy = initialE;
        this.fields1 = generateFields1();
        this.numberOfGenes = numberOfGenes;
    }

    public LinkedHashMap<Vector2d, InfoField> generateFields1() {
        LinkedHashMap<Vector2d, InfoField> Field = new LinkedHashMap<>();
        for (int i = low.x ; i <= high.x; i++){
            for (int j = low.y ; j <= high.y; j++) {
                InfoField info = new InfoField(new Vector2d(i, j));
                Field.put(new Vector2d(i, j), info);
            }
        }
        return Field;
    }

    public int freeFields() {
        int result = 0;
        for (InfoField info: fields1.values()){
            if (info.elements == 0) result += 1;
        }
        this.freeFields = result;
        return this.freeFields;
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.low) && position.precedes(this.high);
    }

    @Override
    public void positionChanged(Vector2d oldPos, Vector2d newPos, Animal animal) {
        LinkedList<Animal> animalsOldPlace = this.animals.get(oldPos);
        this.animals.computeIfAbsent(newPos, k -> new LinkedList<>());
        LinkedList<Animal> animalsNewPlace = this.animals.get(newPos);
        animalsOldPlace.remove(animal);
        animalsNewPlace.add(animal);
    }

    public Vector2d HellsPortal(){
        int x = (int) (Math.random() * (high.x-low.x+1) + low.x);
        int y = (int) (Math.random() * (high.y-low.y+1) + low.y);
        return new Vector2d(x, y);
    }

    public void moveAll() {
        for (Animal animal: listOfAnimals) animal.move();
    }

    public void place(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (!(pos.x >= this.width || pos.x < 0 || pos.y >= this.height || pos.y < 0)){
            this.addAnimal(animal, pos);
            animal.addObserver(this);
        }
    }

    public Grass grassAt(Vector2d position) {
        return grasses.get(position);
    }

    // usuwanie i dodawanie zwierząt
    public void addAnimal(Animal animal, Vector2d newPosition){
        fields1.get(newPosition).incrementElementsStatus();
        if (animals.get(newPosition) == null) {
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animals.put(newPosition, list);
            listOfAnimals.add(animal);
        }
        else {
            animals.get(newPosition).add(animal);
            listOfAnimals.add(animal);
        }
    }

    public void removeAnimal(Animal animal, Vector2d oldPosition) {
        fields1.get(oldPosition).decrementElementsStatus();
        if (this.animals.get(oldPosition) != null) {
            this.animals.get(oldPosition).remove(animal);
            this.listOfAnimals.remove(animal);
            animal.removeObserver(this);
            if (animals.get(oldPosition).size() == 0) animals.remove(oldPosition);
        }
    }

    public void removeDead() {
        CopyOnWriteArrayList<Animal> bodiesToRemove = new CopyOnWriteArrayList<>();
        for (Vector2d position: animals.keySet()){
            for (Animal animal: animals.get(position)){
                if (animal != null && animal.isDead()) {
                    InfoField info = fields1.get(animal.getPosition());
                    info.incrementDeathStatus();
                    animal.isDead = animal.daysOfLife;
                    deadAnimals += 1;
                    livingAnimals -= 1;
                    daysOfLifeDeadsSum += animal.daysOfLife;
                    bodiesToRemove.add(animal);
                }
            }
        }
        for (Animal dead : bodiesToRemove) {
            removeAnimal(dead, dead.getPosition());
        }
    }

    // grass operations
    public void removeGrass(Grass grass) {
        grasses.remove(grass.getPosition());
        InfoField info = fields1.get(grass.getPosition());
        info.decrementElementsStatus();
    }

    abstract public void plantGrass();

    // reproduction
    public List<Animal> getStrongest(Vector2d position, int howMany){
        List<Animal> parents = animals.get(position);
        parents.sort(Comparator.comparing(Animal::getCurrentEnergy)
                .thenComparing(Animal::getDaysOfLife)
                .thenComparing(Animal::getNumberOfChildren));
        Collections.reverse(parents);
        return parents.subList(0, howMany);
    }

    public Animal getBaby(Animal stronger, Animal weaker){
        return new Animal(this, stronger, weaker);
    }

    public void reproduction() {
        ArrayList<Animal> toUpdate = new ArrayList<>();
        for (Vector2d position : animals.keySet()){
            if ( animals.get(position) != null && animals.get(position).size() >= 2) {
                List<Animal> parents = getStrongest(position, 2);
                Animal stronger = parents.get(0);
                Animal weaker = parents.get(1);
                stronger.addNewChild();
                weaker.addNewChild();
                if (weaker.getCurrentEnergy() >= minReproductionEnergy) {
                    Animal baby = getBaby(parents.get(0), parents.get(1)); // tablica jest posortowana
                    stronger.reproduce(weaker);
                    toUpdate.add(baby);
                    InfoField info = fields1.get(baby.getPosition());
                    info.incrementElementsStatus();
                }
            }
        }
        for (Animal baby: toUpdate) this.place(baby);
    }

    // eating
    public void feed(Animal animal){
        animal.setEnergy(animal.getCurrentEnergy() + plantEnergy);
        animal.atePlant();
    }

    public void eat() {
        List<Grass> toUpdate = new ArrayList<>();
        for (Vector2d position: animals.keySet()){
            if (grassAt(position) != null && animals.get(position) != null && animals.get(position).size() > 0){
                Animal strongestAnimal = getStrongest(position, 1).get(0);
                feed(strongestAnimal);
                toUpdate.add(grasses.get(position));
            }
        }
        for (Grass element : toUpdate) { // ConcurrentModification
            fields1.get(element.getPosition()).decrementElementsStatus();
            removeGrass(element);
            plantsNumber -= 1;
        }
    }

    // new day
    public void nextDay() {
        this.day += 1;
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) animal.aliveNextDay();
        }
    }

    public int getInitialEnergy() {
        return this.initialEnergy;
    }
}