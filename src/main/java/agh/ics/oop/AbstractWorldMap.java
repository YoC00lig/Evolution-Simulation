package agh.ics.oop;
import java.util.*;

public class AbstractWorldMap implements IPositionChangeObserver{
    public Vector2d low, high;
    public int width, height, minReproductionEnergy, plantEnergy, epoch, initialEnergy, moveEnergy;
    protected LinkedHashMap<Vector2d, LinkedList<Animal>> animals = new LinkedHashMap<>();
    public ArrayList<Animal> listOfAnimals = new ArrayList<>();
    public LinkedHashMap<Vector2d, Grass> grasses = new LinkedHashMap<>();
    protected LinkedList<Vector2d> fields;
    protected boolean predistinationMode, toxicDeadMode, isCrazyMode, hellExistsMode;
    //for statistics
    int deadAnimals = 0;
    int daysOfLifeDeadsSum = 0;
    int livingAnimals = 0;
    int plantsNumber = 0;
    int dominantGenotype = 0;
    int averageEnergy, averageLifeLength, freeFields;


    public AbstractWorldMap(int width, int height,boolean predistination, boolean toxicMode, boolean isCrazyMode, boolean hellExistsMode, int reproductionE, int plantE, int initialE, int moveE) {
        this.width = width;
        this.height = height;
        this.low = new Vector2d(0,0);
        this.high = new Vector2d(this.width-1,this.height-1);
        this.epoch = 0;
        this.freeFields = this.width * this.height;
        this.predistinationMode = predistination;
        this.toxicDeadMode = toxicMode;
        this.isCrazyMode = isCrazyMode;
        this.hellExistsMode = hellExistsMode;
        this.fields = getFields(); // ułatwi wybór pola o najmniejszej liczbie śmierci
        this.plantEnergy = plantE;
        this.minReproductionEnergy = reproductionE;
        this.initialEnergy = initialE;
        this.moveEnergy = moveE;
    }

    // for statistics
    public void averageEnergy() {
        int numberOfAnimals = 0;
        int allenergy = 0;
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                numberOfAnimals += 1 ;
                allenergy += animal.getCurrentEnergy();
            }
        }
        this.averageEnergy = allenergy / numberOfAnimals;
    }

    public void averageLifeLength() {
        if (deadAnimals >= 1) this.averageLifeLength = daysOfLifeDeadsSum / deadAnimals;
        else this.averageLifeLength = 1;
    }

    public void freeFields() {
        int result = 0;
        for (Vector2d field : fields){
            if (field.elements == 0) result += 1;
        }
        this.freeFields = result;
    }

    public void findDominantGenotype() {
        int[] cnt = new int[8];
        for (Animal animal: listOfAnimals) {
            int gen = animal.findDominantGenotype();
            cnt[gen] += 1;
        }
        int maxi = 0;
        int Gen = 0;

        for (int i = 0; i < 8; i++) {
            if (cnt[i] > maxi) {
                Gen = i;
                maxi = cnt[i];
            }
        }
        this.dominantGenotype = Gen;
    }

    public ArrayList<Animal> animalsWithDominantGenotype() {
        ArrayList<Animal> resultAnimals = new ArrayList<>();
        for (Animal animal: listOfAnimals) {
            if (animal.findDominantGenotype() == this.dominantGenotype) resultAnimals.add(animal);
        }
        return resultAnimals;
    }

    // get all fields
    public LinkedList<Vector2d> getFields() {
        LinkedList<Vector2d> fields = new LinkedList<>();
        for (int i = low.x ; i <= high.x; i++){
            for (int j = low.y ; j <= high.y; j++) fields.add(new Vector2d(i, j));
        }
        return fields;
    }

    // positions checking etc
    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.low) && position.precedes(this.high);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.removeAnimal(animal, oldPosition);
        this.addAnimal(animal, newPosition);
    }

    public Vector2d HellsPortal(){ // piekielny portal - przerzucamy zwierzątko w losowe miejsce
        int x = (int) (Math.random() * (high.x - low.x));
        int y = (int) (Math.random() * (high.y - low.y));
        return new Vector2d(x, y);
    }

    public void place(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (!(pos.x >= this.width || pos.x < 0 || pos.y >= this.height || pos.y < 0)){
            this.addAnimal(animal, pos);
            animal.addObserver(this);
        }
    }

    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (animals.get(position) == null) {
            return grasses.get(position);
        }
        return animals.get(position).get(0);
    }

    public AbstractWorldMapElement grassAt(Vector2d position) {
        return grasses.get(position);
    }

    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    // animal general functions
    public void moveAllAnimals() {
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) {
                animal.move2();
                animal.reduceEnergy();
            }
        }
    }

    public void addAnimal(Animal animal, Vector2d newPosition){
        if (animals.get(newPosition) == null) {
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animals.put(newPosition, list);
            listOfAnimals.add(animal);
        }
        else animals.get(newPosition).add(animal);
    }

    public void removeAnimal(Animal animal, Vector2d oldPosition) {
        LinkedList<Animal> list =  animals.get(oldPosition);
        if (animals.get(oldPosition) != null) {
            list.remove(animal);
            listOfAnimals.remove(animal);
            if (list.size() == 0) animals.remove(oldPosition);
        }
    }


    public void removeDead() {
        for (Vector2d position: animals.keySet()){
            for (Animal animal: animals.get(position)){
                if (animal.isDead()) {
                    animal.getPosition().incrementDeathStatus(); // umarło zwierzątko, czyli na tej pozycji zwiększa sie liczba smierci
                    int index = fields.indexOf(animal.getPosition());
                    if (index != -1) {
                        fields.get(index).death = animal.getPosition().death; // aktualizujemy liczbę śmierci na danym polu
                        fields.get(index).elements -= 1; // jeden element mniej w danym polu
                    }
                    animal.isDead = animal.daysOfLife; // umarło, oznaczamy ile dni żyło
                    deadAnimals += 1;
                    daysOfLifeDeadsSum += animal.daysOfLife;
                    removeAnimal(animal, position);
                }
            }
        }
    }

    // grass operations
    public void removeGrass(Grass grass) {
        grasses.remove(grass.getPosition(), grass);
    }

    public void plantGrass() { // funkcja zasadza jedną roślinkę

        if (toxicDeadMode) { // wariant "toksyczne trupy"
            fields.sort(new ComparatorForFieldDeath());
            for (Vector2d v : fields) {
                if (!(grassAt(v) instanceof Grass)) {
                    Grass element = new Grass(v, this);
                    grasses.put(v, element);
                    plantsNumber += 1;
                    break;
                }
            }
        }

        else { // wariant "zalesione równiki"
            int middle = this.width / 2;
            System.out.println("the middle is: " + middle);
            boolean planted = false;

            for (int col = this.low.y; col <= this.high.y; col++) { // sadzimy na równiku
                Vector2d v = new Vector2d(middle, col);
                if (!(grassAt(v) instanceof Grass)) {
                    Grass element = new Grass(v, this);
                    System.out.println("planted grass at " + v.toString());
                    grasses.put(v, element);
                    planted = true;
                    plantsNumber += 1;
                    break;
                }
            }
            if (!planted){ // nie udało się zasadzić na równiku, wiec sadzimy gdzieś indziej
                for (int row = this.low.x; row <= this.high.x; row++){
                    for (int col = this.low.y; col <= this.high.y; col++){
                        if (row != middle){
                            Vector2d v = new Vector2d(row, col);
                            if (!(grassAt(v) instanceof Grass)) {
                                Grass element = new Grass(v, this);
                                System.out.println("planted grass at " + v.toString());
                                grasses.put(v, element);
                                plantsNumber += 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    // reproduction
    public List<Animal> getParents(Vector2d position){
        List<Animal> parents = animals.get(position);
        parents.sort(new ComparatorForEnergy());
        return parents.subList(0, 2);
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

                stronger.addNewChild();
                weaker.addNewChild();

                if (weaker.getCurrentEnergy() >= minReproductionEnergy) {
                    Animal baby = getBaby(parents);
                    stronger.reproduce(weaker);
                    int index = fields.indexOf(baby.getPosition());
                    if (index != -1) {
                        fields.get(index).elements += 1;
                    }
                    this.place(baby);
                }
            }
        }
    }

    // eating
    public void rewrite(List<Animal> toUpdate) {
        for (Animal animal: toUpdate) {
            removeAnimal(animal, animal.getPosition());
            addAnimal(animal, animal.getPosition());
        }
    }
    public void feed(List<Animal> Animals){
        List<Animal> toUpdate = new ArrayList<>();
        int gained = (int) Math.floor((float) plantEnergy / Animals.size());

        for (Animal animal:Animals){
            animal.setEnergy(animal.getCurrentEnergy() + gained);
            animal.atePlant();
            toUpdate.add(animal);
        }
        rewrite(toUpdate);
    }

    public List<Animal> findStrongestAtPos(Vector2d position) {
        this.animals.get(position).sort(new ComparatorForEnergy());
        List<Animal> list = this.animals.get(position);
        Animal strongest = list.get(0);

        int idx = 1;
        while (idx < list.size()){
            Animal current = list.get(idx);
            if (strongest.getCurrentEnergy() == current.getCurrentEnergy()) idx++;
        }
        return list.subList(0, idx);
    }

    public void eat() {
        List<Grass> toUpdate = new ArrayList<>();
        for (Vector2d position : grasses.keySet()){
            if (this.animals.get(position) != null && this.animals.get(position).size() > 0) {
                List<Animal> strongestAnimals = findStrongestAtPos(position);
                feed(strongestAnimals);
                toUpdate.add(grasses.get(position));
            }
        }
        for (Grass element : toUpdate) {
            removeGrass(element);
            plantsNumber -= 1;
        }
    }

    // new day
    public void nextDay() {
        this.epoch+=1;
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) animal.aliveNextDay();
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


    // change mode
    public void changePredistinationMode(boolean isPredistinationMode){
        this.predistinationMode = isPredistinationMode;
    }

    public void changeIsCrazyMode(boolean isCrazyMode){
        this.isCrazyMode = isCrazyMode;
    }

    public void changeHellExistsMode(boolean isHellExistsMode){
        this.hellExistsMode = isHellExistsMode;
    }

    public void changeToxicDeadMode(boolean isToxicDeadMode){
        this.toxicDeadMode = isToxicDeadMode;
    }

}
