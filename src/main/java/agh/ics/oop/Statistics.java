package agh.ics.oop;

public class Statistics {
    AbstractWorldMap map;

    public Statistics(AbstractWorldMap map){
        this.map = map;
    }

    public void averageEnergy() {
        int numberOfAnimals = 0;
        int allenergy = 0;
        for (Vector2d position : map.animals.keySet()) {
            for (Animal animal : map.animals.get(position)) {
                numberOfAnimals += 1 ;
                allenergy += animal.getCurrentEnergy();
            }
        }
        map.averageEnergy = allenergy / numberOfAnimals;
    }

    public void averageLifeLength() {
        map.removeDead();
        if (map.deadAnimals >= 1) map.averageLifeLength = map.daysOfLifeDeadsSum / map.deadAnimals;
    }

    public void findDominantGenotype() {
        int[] cnt = new int[8];
        for (Animal animal: map.listOfAnimals) {
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
        map.dominantGenotype = Gen;
    }

}
