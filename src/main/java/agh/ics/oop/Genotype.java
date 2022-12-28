package agh.ics.oop;

import java.util.List;

public class Genotype {
    private static final int typesOfGenes = 8;

    public static int[] createDNA(int numberOfGenes) {
        int[] genes = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++){
            genes[i] = (int) (Math.random() * typesOfGenes);
        }
        return genes;
    }

    public static int[] getChildGenotype(Animal parent1, Animal parent2, boolean isCrazyMode, int numberOfGenes, AbstractWorldMap map) {
        int energy1 = parent1.getCurrentEnergy();
        int energy2 = parent2.getCurrentEnergy();
        int[] genes = new int[numberOfGenes];
        int rightSide = (int) (Math.random() * 2);
        List<Animal> parents = map.getStrongest(parent1.getPosition(), 2);
        Animal stronger = parents.get(0);
        Animal weaker = parents.get(1);

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

        return genes;
    }

    public static int changeGene(int gen) {
        switch (gen){
            // w przypadku 0 i 7 mozemy tylko zmienic w jeden sposob
            case 0 -> {return 1;}
            case 7 -> {return 6;}
            default -> {
                int ans = incrementOrNot();
                if (ans == 0) return gen - 1; // jesli wylosujemy 0 to zmniejszamy wartosc, jesli 1 to zwiekszamy
                else return  gen + 1;
            }
        }
    }

    public static int incrementOrNot() {
        return (int) (Math.random() * 2);
    }

    public String toString(int[] genotype) {
        StringBuilder genotypeString = new StringBuilder();
        for (int gen: genotype){
            genotypeString.append(gen);
        }
        return genotypeString.toString();
    }
}
