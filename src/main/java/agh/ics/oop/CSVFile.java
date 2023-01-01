package agh.ics.oop;


import java.io.*;

public class CSVFile {
    AbstractWorldMap map;
    String path;
    static File file;

    public CSVFile(String path, AbstractWorldMap map) {
        this.path = path;
        this.map = map;
        file = new File(path);
    }

    public void update() {
        Statistics stats = new Statistics(this.map);
        saveRecord(this.map.day, this.map.livingAnimals, this.map.plantsNumber, this.map.freeFields(), stats.findDominantGenotype(), stats.averageEnergy(), stats.averageLifeLength());
    }

    public static void saveRecord(int day, int livingAnimals, int plantsNumber, int freeFields, int mostPopularGenotype, int avgEnergy, int avgLifeLength){
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println("Day: " + day + ", Living animals: " +  livingAnimals + ", plants number: " + plantsNumber +", FreeFields: " + freeFields + ", most popular genotype: " +
                    mostPopularGenotype + ", Average energy: " + avgEnergy + ", Average life length: " + avgLifeLength);

            pw.flush();
            pw.close();

        } catch (Exception E){
            throw new RuntimeException("Cannot save data");
        }
    }
}
