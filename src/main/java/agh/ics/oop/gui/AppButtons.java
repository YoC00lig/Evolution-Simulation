package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class AppButtons {
    HBox box;

    public AppButtons(Thread thread) {
        Button exitButton = new Button("EXIT");
        Button stopButton = new Button("STOP");
        box = new HBox(exitButton, stopButton);
        exitButton.setStyle("-fx-background-color: #ff6666");
        stopButton.setStyle("-fx-background-color: #ff6666");


        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        exitButton.setOnAction(event -> {
            System.exit(0);
        });
    }

    public HBox getBox(){
        return this.box;
    }
}
