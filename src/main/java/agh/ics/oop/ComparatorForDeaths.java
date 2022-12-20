package agh.ics.oop;
import java.util.Comparator;

public class ComparatorForDeaths implements Comparator<InfoField>{

    @Override
    public int compare(InfoField i1, InfoField i2){
        return Integer.compare(i1.death, i2.death);
    }
}
