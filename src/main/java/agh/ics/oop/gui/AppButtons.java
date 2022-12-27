package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
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

    public AppButtons(SimulationEngine engine) {
        exitButton = new Button("EXIT");
        stopButton = new Button("STOP");
        this.box = new HBox(exitButton, stopButton);
        exitButton.setStyle("-fx-background-color: #ff6666");
        stopButton.setStyle("-fx-background-color: #ff6666");
        exitButton.setCancelButton(true);

        EventHandler<MouseEvent> eventHandler = e -> {
            stopButton.setEffect(new DropShadow());
            if (engine.getStatus()){
                stopButton.setText("START");
                engine.deactivate();
            }
            else {
                stopButton.setText("STOP");
                engine.activate();
            }
            stopButton.setEffect(null);
        };

        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        exitButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    exitButton.setEffect(new DropShadow());
//                    ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                    Stage stage = ((Stage) (((Button) e.getSource()).getScene().getWindow()));
//                    stage.setScene(app.getScene2());
//                    System.exit(0);
                    stage.close();
                });


        stopButton.addEventHandler(MouseEvent.MOUSE_ENTERED, eventHandler);
    }

    public HBox getBox(){
        return this.box;
    }

}