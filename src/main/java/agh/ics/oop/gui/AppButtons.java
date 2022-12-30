package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppButtons {
    HBox box;
    Button exitButton;
    Button stopButton;
    Button saveButton = new Button("SAVE DATA");

    public AppButtons(SimulationEngine engine, Stage stage, Thread thread) {
        exitButton = new Button("EXIT");
        stopButton = new Button("STOP");
        stopButton.setMinWidth(80);
        exitButton.setMinWidth(80);
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
        };

        box.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    exitButton.setEffect(new DropShadow());
                    ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                    stage.close();
                    thread.stop();
                });

        stopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        styleButtonHover(stopButton);
        styleButtonHover(exitButton);
    }

    public HBox getBox(){
        return this.box;
    }

    public Button getSaveButton() {
        saveButton.setStyle("-fx-background-color: #ff6666");
        saveButton.setMaxWidth(100);
        saveButton.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(saveButton, new Insets(50,0,0,0));
        return saveButton;
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    B.setEffect(new DropShadow());
                });
        B.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    B.setEffect(null);
                });
    }
}