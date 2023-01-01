package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;

public class ToxicMap extends AbstractWorldMap{
    protected ArrayList<InfoField> informations = new ArrayList<>(); // ArrayList żeby móc łatwo posortować po polu death

    public ToxicMap(int width, int height, boolean predistination, boolean isCrazyMode, boolean hellExistsMode, int reproductionE, int plantE, int initialE, int genesNumber, int minMut, int maxMut, int reprCost) {
        super(width, height, predistination, isCrazyMode, hellExistsMode, reproductionE, plantE, initialE, genesNumber, minMut, maxMut, reprCost);
        this.informations.addAll(fields1.values()); // interesują nas tylko informacje o liczbie śmierci na samym polu, więc potrzebujemy tylko InfoField, nie potrzebujemy się odwoływać po wektorach
    }

    @Override
    public void plantGrass() {
        int ans = (int) (Math.random() * 10);
        this.informations.sort(new ComparatorForDeaths());
        int breakIndex = (this.informations.size()*2)/10;
        switch (ans) {
            case 0,1 -> {
                boolean planted = plantGrassInFieldFromGivenRange(breakIndex, this.informations.size());
                if (!planted) plantGrassInFieldFromGivenRange(0, breakIndex);
            }
            default -> {
                boolean planted = plantGrassInFieldFromGivenRange(0, breakIndex);
                if (!planted) plantGrassInFieldFromGivenRange(breakIndex, this.informations.size());
            }
        }
    }

    public boolean plantGrassInFieldFromGivenRange(int idx1, int idx2) { // [idx1, idx2)
        Collections.shuffle(informations.subList(idx1, idx2));
        for (int i = idx1; i < idx2; i++){
            InfoField info = this.informations.get(i);
            Vector2d v = info.position;
            if (grassAt(v) == null) {
                new Grass(v, this);
                return true;
            }
        }
        return false;
    }

}

