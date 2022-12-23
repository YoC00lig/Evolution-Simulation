package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class EquatorMap extends AbstractWorldMap {
    private final TreeMap<Integer, LinkedList<Vector2d>> fields2; // Posortowane według klucza - od najbliższych od równika do najdalszych od równika
    private final ArrayList<Vector2d> preferForEquator; // 20% miejsc na mapie preferowanych w wariancie z równikiem
    private final ArrayList<Vector2d> notPreferForEquator; // pozostałe miejsca na mapie, które nie są preferowane

    public EquatorMap( int width, int height, boolean predistination, boolean isCrazyMode,
        boolean hellExistsMode, int reproductionE, int plantE, int initialE){
            super(width, height, predistination, isCrazyMode, hellExistsMode, reproductionE, plantE, initialE);
            this.fields2 = generateFields2();
            this.preferForEquator = classifyToPreferField();
            this.notPreferForEquator = classifyToNotPreferField();
        }

    public void plantGrass() {// wariant "zalesione równiki
        int ans = (int) (Math.random() * 10);
        switch (ans) {
            case 0, 1 -> this.plantGrassRandomly(); // 20% szans że wyrośnie w innym miejscu
            default -> this.plantGrassAtEquator(); // 80% szans że wyrośnie w preferowanym miejscu
        }
    }

    public TreeMap<Integer, LinkedList<Vector2d>> generateFields2() { // funkcja pomocnicza do wyznaczania pól preferowanych
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

    // miejsca preferowane na mapie i miejsca niepreferowane - klasyfikacja do odpowiedniej grupy
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
        ArrayList<Vector2d> notPreferList = new ArrayList<>();
        for (LinkedList<Vector2d> list: fields2.values()){
            for (Vector2d v: list){
                if (!preferForEquator.contains(v)) notPreferList.add(v);
            }
        }
        return notPreferList;
    }

    // sadzenie trawy w miejscu preferowanym - równik
    public void plantGrassAtEquator() {
        boolean planted = false;
        Collections.shuffle(preferForEquator);
        for (Vector2d v: preferForEquator){
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
        Collections.shuffle(notPreferForEquator);
        for (Vector2d v: notPreferForEquator){
            if (grassAt(v) == null) {
                new Grass(v, this);
                planted = true;
                break;
            }
        }
        if (!planted) plantGrassAtEquator(); // jesli nie uda się zasadzic bo np nie ma miejsc, to ostatecznie sadzimy na rowniku
    }
}
