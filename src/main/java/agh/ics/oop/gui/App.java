package agh.ics.oop.gui;

import agh.ics.oop.IEngine;
import agh.ics.oop.SimulationEngine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    private Scene scene;
    public Map<IEngine, EvolutionWindow> windows = new HashMap<>();
    Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        StartScene startscene = new StartScene();
        Button startButton = startscene.getStartButton();
        startButton.setOnAction(event -> {
            scene = new InputsScene(primaryStage, this).getScene();
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        Scene sceneStart = startscene.getScene();
        primaryStage.setScene(sceneStart);
        primaryStage.show();

    }

    public void draw(SimulationEngine engine) throws FileNotFoundException {
        Platform.runLater(() -> {
            try {
                windows.get(engine).drawGame();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

