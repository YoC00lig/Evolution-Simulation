package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Genotype;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AnimalStatistics {
    private final Animal animal;
    private final VBox statistics;


    public AnimalStatistics(Animal animal) {
        this.animal = animal;
        Label title = new Label("Animal information");
        title.setFont(new Font("Verdana", 24));
        VBox.setMargin(title, new Insets(0,0,60,0));
        Label genotype = new Label("Genotype: " + new Genotype().toString(animal.getGenotype()));
        Label activeGen = new Label("Active Gen: " + animal.getActiveGen());
        Label currEnergy = new Label("Current energy: " + animal.getCurrentEnergy());
        Label eatenPlants = new Label("Eaten plants: " + animal.getEatenPlants());
        Label childrenNum = new Label("Children number: " + animal.getNumberOfChildren());
        Label daysOfLife = new Label("Days of life: " + animal.getDaysOfLife());
        Label alive = new Label(checkIfAlive());
        Image image;

        try {
            image = new Image(new FileInputStream(animal.getPath(animal)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ImageView snail = new ImageView(image);
        snail.setFitWidth(50);
        snail.setFitHeight(50);

        setFonts(genotype, activeGen, currEnergy, eatenPlants, childrenNum, daysOfLife, alive);
        this.statistics = new VBox(title, genotype, activeGen, currEnergy, eatenPlants, childrenNum, daysOfLife, alive, snail);
        statistics.setAlignment(Pos.CENTER);
        statistics.setStyle("-fx-background-color: #f7cac9");
    }

    public String checkIfAlive() {
        String result;
        if (animal.isAlive()) result = "Animal is still alive";
        else result = "Death day: " + animal.getDaysOfLife();
        return result;
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

    public VBox getBox(){return this.statistics;}
}
