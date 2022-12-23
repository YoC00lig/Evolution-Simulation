package agh.ics.oop;

import agh.ics.oop.AbstractWorldMap;

public class EquatorMap extends AbstractWorldMap {

        int ans = (int) (Math.random() * 10);
    public EquatorMap( int width, int height, boolean predistination, boolean isCrazyMode,
        boolean hellExistsMode, int reproductionE, int plantE, int initialE){
            super(width, height, predistination, isCrazyMode, hellExistsMode, reproductionE, plantE, initialE);
        }
    public void plantGrass() {// wariant "zalesione równiki
        switch (ans) {
            case 0, 1 -> this.plantGrassRandomly(); // 20% szans że wyrośnie w innym miejscu
            default -> this.plantGrassAtEquator(); // 80% szans że wyrośnie w preferowanym miejscu


        }
    }
}
