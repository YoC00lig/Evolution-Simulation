package agh.ics.oop;
import java.util.Arrays;

public class Genotype {
    private static final int numberOfGenes = 32;
    private static final int typesOfGenes = 8;


    public static int[] createDNA() {
        int[] genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++){
            genes[i] = (int) (Math.random() * typesOfGenes);
        }
        Arrays.sort(genes);
        return genes;
    }

    public static int[] getChildGenotype(Animal parent1, Animal parent2, boolean isCrazyMode) {
        int energy1 = parent1.getCurrentEnergy();
        int energy2 = parent2.getCurrentEnergy();
        int[] genes = new int[numberOfGenes];
        int rightSide = (int) (Math.random() * 2);
        Animal[] parents = getStrongerWeaker(parent1, parent2);
        Animal stronger = parents[0];
        Animal weaker = parents[1];

        int breakPoint = ((stronger.getCurrentEnergy() * numberOfGenes) / (energy1+energy2));
        int remaining = numberOfGenes - breakPoint;

        switch (rightSide) {
            case 0 -> {
                if (breakPoint >= 0) System.arraycopy(stronger.getGenotype(), 0, genes, 0, breakPoint);
                if (remaining >= 0) System.arraycopy(weaker.getGenotype(), breakPoint, genes, breakPoint, remaining);
            }
            case 1 -> {
                if (remaining >= 0) System.arraycopy(weaker.getGenotype(), 0, genes, 0, remaining);
                if (breakPoint >= 0) System.arraycopy(stronger.getGenotype(), remaining, genes, remaining, breakPoint);
            }
        }

        int number = (int) (Math.random() * numberOfGenes);
        for (int i = 0 ; i < number; i++) {
            int index = (int) (Math.random() * numberOfGenes);

            if (!isCrazyMode) genes[index] = (int) (Math.random() * typesOfGenes);
            else {
                int newGen = changeGene(genes[index]);
                genes[index] = newGen;
            }
        }

        Arrays.sort(genes);
        return genes;
    }

    public static Animal[] getStrongerWeaker(Animal parent1, Animal parent2){
        Animal stronger;
        Animal weaker;

        if (parent1.getCurrentEnergy() >  parent2.getCurrentEnergy()) {
            stronger = parent1;
            weaker = parent2;
        }
        else {
            stronger = parent2;
            weaker = parent1;
        }
        return new Animal[]{stronger, weaker};
    }

    public static int changeGene(int gen) {
        switch (gen){
            case 0 -> {
                return 1;
            }
            case 7 -> {
                return 6;
            }
            default -> {
                int ans = ifIncrement();
                if (ans == 0) return gen - 1;
                else return  gen + 1;
            }
        }
    }

    public static int ifIncrement() {
        return (int) (Math.random() * 2);
    }
}
