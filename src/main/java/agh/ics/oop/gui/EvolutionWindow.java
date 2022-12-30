package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class EvolutionWindow {
    private AppButtons buttons;
    private HBox boxWithButtons;
    private StatisticsReport  statisticsReport;

    private HBox mainbox;
    private AbstractWorldMap map;
    private SimulationEngine engine;
    private Thread thread;
    private GridPane gridPane = new GridPane();
    private final BorderPane border = new BorderPane();
    private Stage window;
    private Scene scene;
    final int size = 25; // rozmiar mapy
    private final LineCharts animalsNumber = new LineCharts("Animals number", "Animals");
    private final LineCharts plantsNumber = new LineCharts("Plants number", "Plants");
    private final LineCharts avgEnergy = new LineCharts("Average animal energy", "Energy");
    private final LineCharts avgLifeLength = new LineCharts("Average life length", "Life length [days]");
    private final LineCharts freeFields = new LineCharts("Free fields on the map", "Free fields");



    public EvolutionWindow(AbstractWorldMap map, int startAnimalsNum, int startPlantsNum, int dailyGrown, SimulationEngine engine) {
        this.map = map;
        this.engine = engine;
//        engine.activate();
        this.window = new Stage();
        this.buttons = new AppButtons(engine);
        this.boxWithButtons = buttons.getBox();
        this.statisticsReport = new StatisticsReport(map);
        VBox StatsButtons = new VBox(boxWithButtons);
        mainbox = new HBox(gridPane, StatsButtons);
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #eea29a;");
        scene = new Scene(mainbox, 2000,1000);
        scene.setRoot(mainbox);
        window.setScene(scene);

        window.show();
    }


    public void updateCharts() {
        animalsNumber.handler(1, map);
        plantsNumber.handler(2, map);
        freeFields.handler(3, map);
        avgEnergy.handler(4, map);
        avgLifeLength.handler(5, map);
    }

    public void drawGame() throws FileNotFoundException {
        updateCharts();
        statisticsReport.updateStatistics();
        drawMap();
        scene.setRoot(mainbox);
        window.setScene(scene);
        window.show();
    }

    public void drawMap() {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(false);

        for (int i = map.low.x; i <= map.high.x; i++){
            Label numberX = new Label("");
            gridPane.add(numberX,  i - map.low.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            GridPane.setHalignment(numberX, HPos.CENTER);
        }

        for (int i = map.low.y; i <= map.high.y; i++){
            Label numberY = new Label("");
            gridPane.add(numberY, 0,map.high.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            GridPane.setHalignment(numberY, HPos.CENTER);
        }

        for (Animal element: map.listOfAnimals){
            GuiElementBox guiElement = new GuiElementBox(element);
            VBox elem = guiElement.getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - map.low.x + 1, map.high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
            elem.setOnMouseExited(event -> handle(element));
            ImageView view = guiElement.getImageView();
            energyVisualizer(element, view);
        }

        for (Grass element : map.grasses.values()){
            VBox elem = new GuiElementBox(element).getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - map.low.x + 1, map.high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }

        gridPane.setMaxHeight(Region.USE_PREF_SIZE);
        gridPane.setStyle("-fx-background-color: #f3ffe6;");
        gridPane.setAlignment(Pos.CENTER_LEFT);
        VBox charts = new VBox(animalsNumber.getChart(), plantsNumber.getChart(), freeFields.getChart(),
                avgEnergy.getChart(), avgLifeLength.getChart());
        charts.setAlignment(Pos.CENTER);
        VBox stats = statisticsReport.getStatistics();
        VBox StatsButtons = new VBox(stats, boxWithButtons);
        mainbox = new HBox(gridPane, charts, stats, StatsButtons);
        HBox.setMargin(stats, new Insets(0,0,0,50));
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #eea29a;");
    }

    public void handle(Animal animal) {
        AnimalStatistics stats = new AnimalStatistics(animal);
        Stage stageForAnimal = new Stage();
        Scene sceneForAnimal = new Scene(stats.getBox(), 400, 400);
        stageForAnimal.setScene(sceneForAnimal);
        stageForAnimal.show();
    }
    public void energyVisualizer(Animal animal, ImageView view) {
        int maxEnergy = map.getInitialEnergy();
        float percentage = (float) animal.getCurrentEnergy() / maxEnergy;
        if (0.9F < percentage  && percentage <= 1) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(55, 135, 67, 0.8), 30, 0, 0, 0)");
        else if (0.8F < percentage  && percentage <= 0.9F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(32, 198, 56, 0.8), 30, 0, 0, 0)");
        else if (0.7F < percentage  && percentage <= 0.8F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(7, 237, 41, 0.8), 30, 0, 0, 0)");
        else if (0.6F < percentage  && percentage <= 0.7F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(218, 222, 38, 0.8), 30, 0, 0, 0)");
        else if (0.5F < percentage  && percentage <= 0.6F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(251, 255, 80, 0.8), 30, 0, 0, 0)");
        else if (0.4F < percentage  && percentage <= 0.5F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255, 204, 80, 0.8), 30, 0, 0, 0)");
        else if (0.3F < percentage  && percentage <= 0.4F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(241, 144, 74, 0.8), 30, 0, 0, 0)");
        else if (0.2F < percentage  && percentage <= 0.3F) view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255, 80, 0, 0.8), 30, 0, 0, 0)");
        else view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(234, 14, 14, 1), 30, 0, 0, 0)");
    }
}
