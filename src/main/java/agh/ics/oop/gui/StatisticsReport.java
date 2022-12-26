package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import agh.ics.oop.Statistics;
import javafx.scene.text.Font;

public class StatisticsReport {
    private final VBox statistics;
    private final AbstractWorldMap map;
    private Label animalsNum, plantsNum, freeFields, avgLifeLength, avgEnergy, DeadsNumber, DominantGenotype;

    public StatisticsReport(AbstractWorldMap map) {
        Statistics stats = new Statistics(map);
        this.map = map;
        animalsNum = new Label("Animals number: " + map.listOfAnimals.size());
        plantsNum = new Label("Plants number: " + map.grasses.size());
        freeFields = new Label("Empty fields: " + map.freeFields());
        avgLifeLength = new Label("Average lifespan: " + stats.averageLifeLength());
        avgEnergy = new Label("Average energy: " + stats.averageEnergy());
        DeadsNumber = new Label("Dead animals: " + stats.getDeadAnimals());
        DominantGenotype = new Label("Dominant genotype: " + stats.findDominantGenotype());
        setFonts(animalsNum, plantsNum,freeFields,avgLifeLength,avgEnergy, DeadsNumber, DominantGenotype);
        statistics = new VBox(animalsNum, plantsNum, freeFields, avgLifeLength, avgEnergy, DeadsNumber, DominantGenotype);
    }

    public VBox getStatistics(){
        statistics.setStyle("-fx-background-color: #f7cac9");
        statistics.setSpacing(20);
        statistics.setAlignment(Pos.CENTER);
        statistics.setMaxHeight(500);
        return statistics;
    }

    public void setFonts(Label a1, Label a2, Label a3, Label a4, Label a5, Label a6, Label a7){
        a1.setFont(new Font("Verdana", 14));
        a2.setFont(new Font("Verdana", 14));
        a3.setFont(new Font("Verdana", 14));
        a4.setFont(new Font("Verdana", 14));
        a5.setFont(new Font("Verdana", 14));
        a6.setFont(new Font("Verdana", 14));
        a7.setFont(new Font("Verdana", 14));
    }

    public void updateStatistics(){
        Statistics stats = new Statistics(map);
        animalsNum = new Label("Animals number: " + map.listOfAnimals.size());
        plantsNum = new Label("Plants number: " + map.grasses.size());
        freeFields = new Label("Empty fields: " + map.freeFields());
        avgLifeLength = new Label("Average lifespan: " + stats.averageLifeLength());
        avgEnergy = new Label("Average energy: " + stats.averageEnergy());
        DeadsNumber = new Label("Dead animals: " + stats.getDeadAnimals());
        DominantGenotype = new Label("Dominant genotype: " + stats.findDominantGenotype());
        setFonts(animalsNum, plantsNum,freeFields,avgLifeLength,avgEnergy, DeadsNumber, DominantGenotype);
        statistics.getChildren().clear();
        statistics.getChildren().add(animalsNum);
        statistics.getChildren().add(plantsNum);
        statistics.getChildren().add(freeFields);
        statistics.getChildren().add(avgLifeLength);
        statistics.getChildren().add(avgEnergy);
        statistics.getChildren().add(DeadsNumber);
        statistics.getChildren().add(DominantGenotype);
    }
}