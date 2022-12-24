package agh.ics.oop.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class AppButtons {
    HBox box;
    Button exitButton;
    Button stopButton;

    public AppButtons() {
        exitButton = new Button("EXIT");
        stopButton = new Button("STOP");
        this.box = new HBox(exitButton, stopButton);
        exitButton.setStyle("-fx-background-color: #ff6666");
        stopButton.setStyle("-fx-background-color: #ff6666");
        exitButton.setCancelButton(true);


        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    exitButton.setEffect(new DropShadow());
                    ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                });
    }
    public HBox getBox(){
        return this.box;
    }
}
