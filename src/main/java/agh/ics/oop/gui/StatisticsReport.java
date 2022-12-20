package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import agh.ics.oop.Statistics;
import javafx.scene.text.Font;

public class StatisticsReport {
    VBox statistics;
    Label animalsNum, plantsNum, freeFields, avgLifeLength, avgEnergy, DeadsNumber;

    public StatisticsReport(AbstractWorldMap map) {
        Statistics stats = new Statistics(map);
        animalsNum = new Label("Number of animals on the map: " + map.listOfAnimals.size());
        plantsNum = new Label("Number of plants on the map: " + map.grasses.size());
        freeFields = new Label("Number of free fields on the map: " + map.freeFields());
        avgLifeLength = new Label("Average lifespan of dead animals: " + stats.averageLifeLength());
        avgEnergy = new Label("Average energy: " + stats.averageEnergy());
        DeadsNumber = new Label("Number of dead animals: " + stats.getDeadAnimals());

        setFonts(animalsNum, plantsNum,freeFields,avgLifeLength,avgEnergy, DeadsNumber);

        statistics = new VBox(animalsNum, plantsNum, freeFields, avgLifeLength, avgEnergy, DeadsNumber);
    }

    public VBox getStatistics(){
        statistics.setStyle("-fx-background-color: #f7cac9");
        statistics.setSpacing(20);
        statistics.setAlignment(Pos.CENTER);
        statistics.setMaxHeight(500);
        return statistics;
    }

    public void setFonts(Label a1, Label a2, Label a3, Label a4, Label a5, Label a6){
        a1.setFont(new Font("Verdana", 14));
        a2.setFont(new Font("Verdana", 14));
        a3.setFont(new Font("Verdana", 14));
        a4.setFont(new Font("Verdana", 14));
        a5.setFont(new Font("Verdana", 14));
        a6.setFont(new Font("Verdana", 14));
    }
    public void updateStatistics(AbstractWorldMap map){
        Statistics stats = new Statistics(map);
        animalsNum = new Label("Number of animals on the map: " + map.listOfAnimals.size());
        plantsNum = new Label("Number of plants on the map: " + map.grasses.size());
        freeFields = new Label("Number of free fields on the map: " + map.freeFields());
        avgLifeLength = new Label("Average lifespan of dead animals: " + stats.averageLifeLength());
        avgEnergy = new Label("Average energy: " + stats.averageEnergy());
        DeadsNumber = new Label("Number of dead animals: " + stats.getDeadAnimals());

        setFonts(animalsNum, plantsNum,freeFields,avgLifeLength,avgEnergy, DeadsNumber);

        statistics.getChildren().clear();
        statistics = new VBox(animalsNum, plantsNum, freeFields, avgLifeLength, avgEnergy, DeadsNumber);
    }
}
