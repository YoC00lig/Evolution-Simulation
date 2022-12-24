package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class EquatorMap extends AbstractWorldMap {
    private final ArrayList<Vector2d> preferForEquator = new ArrayList<>(); // 20% miejsc na mapie preferowanych w wariancie z równikiem
    private final ArrayList<Vector2d> notPreferForEquator = new ArrayList<>(); // pozostałe miejsca na mapie, które nie są preferowane

    public EquatorMap( int width, int height, boolean predistination, boolean isCrazyMode,
        boolean hellExistsMode, int reproductionE, int plantE, int initialE, int genesNumber){
            super(width, height, predistination, isCrazyMode, hellExistsMode, reproductionE, plantE, initialE, genesNumber);
            classify();
        }

    public void plantGrass() {// wariant "zalesione równiki
        int ans = (int) (Math.random() * 10);
        switch (ans) {
            case 0, 1 -> this.plantGrassRandomly(); // 20% szans że wyrośnie w innym miejscu
            default -> this.plantGrassAtEquator(); // 80% szans że wyrośnie w preferowanym miejscu
        }
    }

    public void classify() { // podział pól na preferowane i niepreferowane
        // wyznaczanie odległości każdego pola od równika, TreeMap żeby mieć posortowane po kluczu
        // klucz to odległość, 20% najbliższych od równika to preferowane a reszta nie
        TreeMap<Integer, LinkedList<Vector2d>> Field = new TreeMap<>();
        int middle = this.height / 2;
        for (int i = low.x; i <= high.x; i++) {
            for (int j = low.y; j <= high.y; j++) {
                int distance = Math.abs(middle - j);
                Vector2d v = new Vector2d(i, j);
                if (Field.isEmpty() || Field.get(distance) == null) {
                    LinkedList<Vector2d> list = new LinkedList<>();
                    list.add(v);
                    Field.put(distance, list);
                }
                else if (Field.get(distance) != null) Field.get(distance).add(v);
            }
        }
        long numOfPrefer = (((long) this.height * this.width * 2) / 10);
        int cnt = 0;

        for (LinkedList<Vector2d> list : Field.values()) {
            for (Vector2d v : list) {
                if (cnt < numOfPrefer) {
                    this.preferForEquator.add(v);
                    cnt += 1;
                }
                this.notPreferForEquator.add(v);
            }
        }
    }

    // sadzenie trawy w miejscu preferowanym - równik
    public void plantGrassAtEquator() {
        boolean planted = false;
        Collections.shuffle(this.preferForEquator);
        for (Vector2d v: this.preferForEquator){
            if (grassAt(v) == null){
                new Grass(v, this);
                planted = true;
                break;
            }
        }
        if (!planted) plantGrassRandomly(); // jesli nie uda się zasadzic bo np nie ma miejsc, to ostatecznie sadzimy gdzieś indziej
    }


    // sadzenie trawy w miejscu niepreferowanym
    public void plantGrassRandomly() {
        boolean planted = false;
        Collections.shuffle(this.notPreferForEquator);
        for (Vector2d v: this.notPreferForEquator){
            if (grassAt(v) == null) {
                new Grass(v, this);
                planted = true;
                break;
            }
        }
        if (!planted) plantGrassAtEquator(); // jesli nie uda się zasadzic bo np nie ma miejsc, to ostatecznie sadzimy na rowniku
    }
}
