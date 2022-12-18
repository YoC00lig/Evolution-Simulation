package agh.ics.oop;
import java.util.Random;


public class SimulationEngine implements IEngine{
    private final AbstractWorldMap map;
    private final int startAnimalsNumber;
    private final int startGrassnumber;
    private final int dailyGrowersNumber;
    private Statistics stats;

    public SimulationEngine(AbstractWorldMap map, int animalsNumber, int grassNumber, int dailyGrassNumber){
        this.map = map;
        this.stats = new Statistics(map);
        this.startAnimalsNumber = animalsNumber;
        this.startGrassnumber = grassNumber;
        this.dailyGrowersNumber = dailyGrassNumber;

        for (int i = 0; i < startAnimalsNumber; i++){
            Random random = new Random();
            int x = random.nextInt(map.high.x + 1 - map.low.x) + map.low.x;
            int y = random.nextInt(map.high.y + 1 - map.low.y) + map.low.y;
            System.out.println("Hello, Simulation engine here, generated animal start position: " + x + "," + y);
            Vector2d position = new Vector2d(x,y);
            Animal animal = new Animal(this.map, position);
            map.place(animal);
        }
        for (int i = 0; i < grassNumber; i++) map.plantGrass();
    }

    @Override
    public void run() {
        map.removeDead();
        map.moveAll();
        map.eat();
        map.reproduction();
        for (int i = 0; i < dailyGrowersNumber; i++) map.plantGrass();
        // statistics
        map.freeFields();
        stats.averageLifeLength();
        stats.averageEnergy();
        stats.findDominantGenotype();
        map.nextDay();
        System.out.println("Current number of animals: " + map.livingAnimals);
        System.out.println("Current number of grass: " + map.grasses.size());

    }
}
