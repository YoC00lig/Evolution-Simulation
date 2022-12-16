package agh.ics.oop;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbstractWorldMap implements IPositionChangeObserver{
    public Vector2d low, high;
    public int width, height, minReproductionEnergy, plantEnergy, epoch, initialEnergy;
    protected LinkedHashMap<Vector2d, LinkedList<Animal>> animals = new LinkedHashMap<>();
    public ArrayList<Animal> listOfAnimals = new ArrayList<>();
    public LinkedHashMap<Vector2d, Grass> grasses = new LinkedHashMap<>();
    protected LinkedHashMap<Vector2d, InfoField> fields1;
    protected TreeMap<Integer, LinkedList<Vector2d>> fields2;
    protected ArrayList<Vector2d> preferForEquator;
    protected ArrayList<InfoField> informations = new ArrayList<>();
    protected boolean predistinationMode, toxicDeadMode, isCrazyMode, hellExistsMode;
    //for statistics
    int deadAnimals = 0;
    int daysOfLifeDeadsSum = 0;
    int livingAnimals = 0;
    int plantsNumber = 0;
    int dominantGenotype = 0;
    int averageEnergy, averageLifeLength, freeFields;


    public AbstractWorldMap(int width, int height,boolean predistination, boolean toxicMode, boolean isCrazyMode, boolean hellExistsMode, int reproductionE, int plantE, int initialE) {
        this.width = width;
        this.height = height;
        this.low = new Vector2d(0,0);
        this.high = new Vector2d(this.width-1,this.height-1);
        this.epoch = 1;
        this.freeFields = this.width * this.height;
        this.predistinationMode = predistination;
        this.toxicDeadMode = toxicMode;
        this.isCrazyMode = isCrazyMode;
        this.hellExistsMode = hellExistsMode;
        this.fields1 = generateFields1();
        this.fields2 = generateFields2();
        this.plantEnergy = plantE;
        this.minReproductionEnergy = reproductionE;
        this.initialEnergy = initialE;
        this.preferForEquator = classifyToPreferField();
        this.informations.addAll(this.fields1.values());
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

    public TreeMap<Integer, LinkedList<Vector2d>> generateFields2() {
        TreeMap<Integer, LinkedList<Vector2d>> Field = new TreeMap<>();
        int middle = this.height/2;
        for (int i = low.x ; i <= high.x; i++){
            for (int j = low.y ; j <= high.y; j++) {

                int distance = Math.abs(middle - j);
                Vector2d v = new Vector2d(i, j);

                if (Field.isEmpty() || Field.get(distance) == null){
                    LinkedList<Vector2d> list = new LinkedList<>();
                    list.add(v);
                    Field.put(distance, list);
                }

                else if (Field.get(distance) != null) Field.get(distance).add(v);
            }
        }
        return Field;
    }

    public void freeFields() {
        int result = 0;
        for (InfoField info: fields1.values()){
            if (info.elements == 0) result += 1;
        }
        this.freeFields = result;
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(this.low) && position.precedes(this.high);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.removeAnimal(animal, oldPosition);
        this.addAnimal(animal, newPosition);
    }

    public Vector2d HellsPortal(){
        int x = (int) (Math.random() * (high.x-low.x+1) + low.x);
        int y = (int) (Math.random() * (high.y-low.y+1) + low.y);
        return new Vector2d(x, y);
    }

    public void moveAll() {
        for (Animal animal: listOfAnimals) {
            animal.move();
        }
    }
    public void place(Animal animal) {
        Vector2d pos = animal.getPosition();
        if (!(pos.x >= this.width || pos.x < 0 || pos.y >= this.height || pos.y < 0)){
            this.addAnimal(animal, pos);
            animal.addObserver(this);
        }
    }

    public AbstractWorldMapElement grassAt(Vector2d position) {
        return grasses.get(position);
    }

    // animal adding and removing from map

    public void addAnimal(Animal animal, Vector2d newPosition){
        fields1.get(newPosition).incrementElementsStatus();
        if (animals.get(newPosition) == null) {
            LinkedList<Animal> list = new LinkedList<>();
            list.add(animal);
            animals.put(newPosition, list);
            listOfAnimals.add(animal);
        }
        else animals.get(newPosition).add(animal);
    }

    public void removeAnimal(Animal animal, Vector2d oldPosition) {
        fields1.get(oldPosition).decrementElementsStatus();
        LinkedList<Animal> list =  animals.get(oldPosition);

        if (animals.get(oldPosition) != null) {
            list.remove(animal);
            listOfAnimals.remove(animal);
            animal.removeObserver(this);
            if (list.size() == 0) animals.remove(oldPosition);
        }
    }

    public void removeDead() {
        CopyOnWriteArrayList<Animal> bodiesToRemove = new CopyOnWriteArrayList<>();
        for (Vector2d position: animals.keySet()){
            for (Animal animal: animals.get(position)){
                if (animal != null) {
                   if (animal.isDead()) {
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
        }

        for (Animal dead : bodiesToRemove) removeAnimal(dead, dead.getPosition());
    }

    // grass operations
    public void removeGrass(Grass grass) {
        grasses.remove(grass.getPosition(), grass);
    }

    public void plantGrass() { // funkcja zasadza jedną roślinkę

        if (toxicDeadMode) { // wariant toksyczne trupy
            this.informations.sort(new ComparatorForDeaths());
            int breakIndex = (this.informations.size()*2)/10;
            int ans = (int) (Math.random() * 10);
            switch (ans) {

                case 0,1 ->  plantGrassInFieldFromGivenRange(breakIndex, this.informations.size());
                default -> plantGrassInFieldFromGivenRange(0, breakIndex);
            }
        }
        else { // wariant "zalesione równiki
            int ans = (int) (Math.random() * 10);
            System.out.println("ans was: " + ans);
            switch (ans) {

                case 0, 1 -> this.plantGrassRandomly(); // 20% szans że wyrośnie w innym miejscu
                default -> this.plantGrassAtEquator(); // 80% szans że wyrośnie w preferowanym miejscu

            }
        }
    }

    public ArrayList<Vector2d> classifyToPreferField(){
        int allFieldsDouble = this.height * this.width * 2;
        System.out.println("Num of all fields: " + allFieldsDouble);
        long numOfPrefer = (allFieldsDouble/10);
        System.out.println("Num of prefer fields: " + numOfPrefer);
        int cnt = 0;
        ArrayList<Vector2d> preferList = new ArrayList<>();

        while (cnt < numOfPrefer){
            for (LinkedList<Vector2d> list: fields2.values()){
                for (Vector2d v: list){
                    preferList.add(v);
                    cnt += 1;
                    if (cnt == numOfPrefer) return preferList;
                }
            }
        }
        return preferList;
    }

    public void plantGrassAtEquator() {
        for (Vector2d v: preferForEquator){
            if (!(grassAt(v) instanceof Grass)){
                System.out.println("Generated grass at prefer field at position: " + v.toString());
                fields1.get(v).incrementElementsStatus();
                Grass element = new Grass(v, this);
                grasses.put(v, element);
                return;
            }
        }
    }
    public void plantGrassRandomly() {
        boolean planted = false;
        this.freeFields();

        while (!planted || this.freeFields == 0){ // dopóki nie zasadzimy lub zabraknie miejsca na mapie
            this.freeFields();
            Vector2d v = HellsPortal();
            if (!(grassAt(v) instanceof Grass) && !(preferForEquator.contains(v))) {
                System.out.println("Generated grass randomly at position: " + v.toString());
                fields1.get(v).incrementElementsStatus();
                Grass element = new Grass(v, this);
                grasses.put(v, element);
                planted = true;
            }
        }
    }

    public void plantGrassInFieldFromGivenRange(int idx1, int idx2) { // idx2 exclusive
        for (int i = idx1; i < idx2; i++){
            InfoField info = this.informations.get(i);
            Vector2d v = info.position;
            if (!(grassAt(v) instanceof Grass)) {
                Grass element = new Grass(v, this);
                fields1.get(v).incrementElementsStatus();
                grasses.put(v, element);
                return;
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

    // todo - getParents - new comparator
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
                    InfoField info = fields1.get(baby.getPosition());
                    info.incrementElementsStatus();
                    this.place(baby);
                }
            }
        }
    }

    // eating
    // todo
    public void feed(List<Animal> Animals){
        List<Animal> toUpdate = new ArrayList<>();
        int gained = (int) Math.floor((float) plantEnergy / Animals.size());

        for (Animal animal:Animals){
            animal.setEnergy(animal.getCurrentEnergy() + gained);
            animal.atePlant();
            toUpdate.add(animal);
        }
        for (Animal animal: toUpdate) {
            removeAnimal(animal, animal.getPosition());
            addAnimal(animal, animal.getPosition());
        }
    }

    // todo - new comparator
    public List<Animal> findStrongestAtPos(Vector2d position) {
        this.animals.get(position).sort(new ComparatorForEnergy());
        List<Animal> list = this.animals.get(position);
        Animal strongest = list.get(0);

        int idx = 1;
        while (idx < list.size()){
            Animal current = list.get(idx);
            if (strongest.getCurrentEnergy() == current.getCurrentEnergy()) idx++; // todo
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
            fields1.get(element.getPosition()).decrementElementsStatus();
            removeGrass(element);
            plantsNumber -= 1;
        }
    }
    // new day
    public void nextDay() {
        this.epoch += 1;
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) animal.aliveNextDay();
        }
    }

}
