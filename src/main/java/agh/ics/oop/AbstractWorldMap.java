package agh.ics.oop;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Comparator;

public class AbstractWorldMap implements IPositionChangeObserver{
    public Vector2d low, high;
    public int width, height, minReproductionEnergy, plantEnergy, day, initialEnergy;
    protected LinkedHashMap<Vector2d, LinkedList<Animal>> animals = new LinkedHashMap<>();
    public ArrayList<Animal> listOfAnimals = new ArrayList<>();
    public LinkedHashMap<Vector2d, Grass> grasses = new LinkedHashMap<>();
    protected LinkedHashMap<Vector2d, InfoField> fields1; // łatwo można odwoływać się po kluczu do informacji, nie trzeba przeszukiwać całej ArrayList
    protected TreeMap<Integer, LinkedList<Vector2d>> fields2; // Posortowane według klucza
    protected ArrayList<Vector2d> preferForEquator; // 20% miejsc na mapie preferowanych w wariancie z równikiem
    protected ArrayList<Vector2d> notPreferForEquator;
    protected ArrayList<InfoField> informations = new ArrayList<>(); // żeby móc łatwo posortować po polu death
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
        this.day = 1;
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
        this.notPreferForEquator = classifyToNotPreferField();
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

    public TreeMap<Integer, LinkedList<Vector2d>> generateFields2() { // wyznaczamy miejsca preferowane w przypadku wariantu z równikiem
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
        this.removeDead();
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

    public void plantGrass() { // funkcja zasadza jedną roślinkę
        int ans = (int) (Math.random() * 10);

        if (toxicDeadMode) { // wariant toksyczne trupy
            this.informations.sort(new ComparatorForDeaths());
            int breakIndex = (this.informations.size()*2)/10;
            switch (ans) {

                case 0,1 ->  plantGrassInFieldFromGivenRange(breakIndex, this.informations.size());
                default -> plantGrassInFieldFromGivenRange(0, breakIndex);
            }
        }
        else { // wariant "zalesione równiki
            switch (ans) {
                case 0, 1 -> this.plantGrassRandomly(); // 20% szans że wyrośnie w innym miejscu
                default -> this.plantGrassAtEquator(); // 80% szans że wyrośnie w preferowanym miejscu

            }
        }
    }
    // sadzenie trawy
    public ArrayList<Vector2d> classifyToPreferField(){
        int allFieldsDouble = this.height * this.width * 2;
        long numOfPrefer = (allFieldsDouble/10);
        ArrayList<Vector2d> preferList = new ArrayList<>();
        int cnt = 0;

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

    public ArrayList<Vector2d> classifyToNotPreferField() {
        ArrayList<Vector2d> res = new ArrayList<>();
        for (LinkedList<Vector2d> list: fields2.values()){
            for (Vector2d v: list){
                if (!preferForEquator.contains(v)) res.add(v);
            }
        }
        return res;
    }
    public void plantGrassAtEquator() {
        boolean planted = false;
        Collections.shuffle(preferForEquator);
        for (Vector2d v: preferForEquator){
            if (!(grassAt(v) instanceof Grass)){
                Grass element = new Grass(v, this);
                planted = true;
                break;
            }
        }
        if (!planted) plantGrassRandomly();
    }

    public void plantGrassRandomly() {
        boolean planted = false;
        Collections.shuffle(notPreferForEquator);
        for (Vector2d v: notPreferForEquator){
            if (!(grassAt(v) instanceof Grass)) {
                Grass element = new Grass(v, this);
                planted = true;
                break;
            }
        }
        if (!planted) plantGrassAtEquator(); // jesli nie uda się zasadzic bo np nie ma miejsc, to ostatecznie sadzimy na rowniku
    }

    public void plantGrassInFieldFromGivenRange(int idx1, int idx2) { // idx2 exclusive
        for (int i = idx1; i < idx2; i++){
            InfoField info = this.informations.get(i);
            Vector2d v = info.position;
            if (!(grassAt(v) instanceof Grass)) {
                Grass element = new Grass(v, this);
                return;
            }
        }
    }

    // reproduction
    public List<Animal> getParents(Vector2d position){
        List<Animal> parents = animals.get(position);
        parents.sort(Comparator.comparing(Animal::getCurrentEnergy)
                .thenComparing(Animal::getDaysOfLife)
                .thenComparing(Animal::getNumberOfChildren));
        Collections.reverse(parents);
        return parents.subList(0, 2);
    }

    public Animal getBaby(List<Animal> parents){
        Animal parent1 = parents.get(0);
        Animal parent2 = parents.get(1);
        return new Animal(this, parent1, parent2);
    }

    // todo -check
    public void reproduction() {
        ArrayList<Animal> toUpdate = new ArrayList<>();
        for (Vector2d position : animals.keySet()){
            if ( animals.get(position) != null && animals.get(position).size() >= 2) {

                List<Animal> parents = getParents(position);
                Animal stronger = Genotype.getStrongerWeaker(parents.get(0), parents.get(1))[0];
                Animal weaker = Genotype.getStrongerWeaker(parents.get(0), parents.get(1))[1];

                stronger.addNewChild();
                weaker.addNewChild();
                if (weaker.getCurrentEnergy() >= minReproductionEnergy) {
                    Animal baby = getBaby(parents);
                    System.out.println("baby animal. Num of animals: before-" + this.listOfAnimals.size() + " after-" + (this.listOfAnimals.size()+1));
                    stronger.reproduce(weaker);
                    toUpdate.add(baby);
                    InfoField info = fields1.get(baby.getPosition());
                    info.incrementElementsStatus();
                }
            }
        }
        for (Animal baby: toUpdate) this.place(baby);
//        this.removeDead();
    }

    // eating
    // todo - check
    public void feed(Animal animal){
        animal.setEnergy(animal.getCurrentEnergy() + plantEnergy);
        animal.atePlant();
    }

    // todo - check
    public Animal findStrongestAtPos(Vector2d position) {
        this.animals.get(position).sort(Comparator.comparing(Animal::getCurrentEnergy)
                .thenComparing(Animal::getDaysOfLife)
                .thenComparing(Animal::getNumberOfChildren));
        List<Animal> list = this.animals.get(position);
        Collections.reverse(list);
        return list.get(0);
    }

    public void eat() {
        removeDead();
        List<Grass> toUpdate = new ArrayList<>();
        for (Vector2d position: animals.keySet()){
            if (grassAt(position) != null && animals.get(position) != null && animals.get(position).size() > 0){
                Animal strongestAnimal = findStrongestAtPos(position);
                feed(strongestAnimal);
                toUpdate.add(grasses.get(position));
            }
        }
        for (Grass element : toUpdate) { // to avoid ConcurrentModification
            fields1.get(element.getPosition()).decrementElementsStatus();
            removeGrass(element);
            plantsNumber -= 1;
        }
    }
    // new day
    public void nextDay() {
        this.day += 1;
        this.removeDead();
        for (LinkedList<Animal> listOfAnimals: this.animals.values()) {
            for (Animal animal : listOfAnimals) animal.aliveNextDay();
        }
    }

}