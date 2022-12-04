package agh.ics.oop;

public class World {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        int[] genes = Genotype.createDNA();
        for (int num: genes) {
            System.out.println(num);
        }
    }
}
