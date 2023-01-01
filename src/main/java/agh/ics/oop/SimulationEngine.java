package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.io.FileNotFoundException;
import java.util.Random;


public class SimulationEngine implements IEngine, Runnable{
    private final AbstractWorldMap map;
    private final int startAnimalsNumber;
    private final int startGrassnumber;
    private final int dailyGrowersNumber;
    private boolean isActive;
    private final int moveDelay = 300;
    public Statistics stats;
    private final App app;

    public SimulationEngine(AbstractWorldMap map, int animalsNumber, int grassNumber, int dailyGrassNumber, App app){
        this.map = map;
        this.startAnimalsNumber = animalsNumber;
        this.startGrassnumber = grassNumber;
        this.dailyGrowersNumber = dailyGrassNumber;
        this.app =  app;
        this.isActive = true;

        for (int i = 0; i < startAnimalsNumber; i++){
            Random random = new Random();
            int x = random.nextInt(map.high.x + 1 - map.low.x) + map.low.x;
            int y = random.nextInt(map.high.y + 1 - map.low.y) + map.low.y;
            Vector2d position = new Vector2d(x,y);
            new Animal(this.map, position);
            map.livingAnimals += 1;
        }
        for (int i = 0; i < grassNumber; i++) {
            map.plantGrass();
        }
        this.stats = new Statistics(map);
    }

    public void updateMap() {
        if (this.isActive) {
            map.removeDead();
            map.moveAll();
            map.eat();
            map.reproduction();
            for (int i = 0; i < dailyGrowersNumber; i++) map.plantGrass();
            map.freeFields();
            map.nextDay();
        }
    }


    @Override
    public void run() {
        while (map.listOfAnimals.size() >= 0) {
            updateMap();
            try {
                app.draw(this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
                app.decrementCanAgain();
                break;
            }
        }
    }

    public void activate() {this.isActive = true;}
    public void deactivate() {this.isActive = false;}
    public boolean getStatus() {return this.isActive;}

}

