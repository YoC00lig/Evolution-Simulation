package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;



public class EvolutionWindow {
    private AbstractWorldMap mapa;
    private SimulationEngine engine;
    private GridPane gridPane = new GridPane();

    private Stage stage;
    private Scene scene;
    final int size = 25; // rozmiar mapy
    private final LineCharts animalsNumber = new LineCharts("Animals number", "Animals");
    private final LineCharts plantsNumber = new LineCharts("Plants number", "Plants");
    private final LineCharts avgEnergy = new LineCharts("Average animal energy", "Energy");
    private final LineCharts avgLifeLength = new LineCharts("Average life length", "Life length [days]");
    private final LineCharts freeFields = new LineCharts("Free fields on the mapa", "Free fields");
    private AppButtons buttons;
    private HBox boxWithButtons;
    private StatisticsReport  statisticsReport;

        private int startAnimalsNum;
    private HBox mainbox;

    private Thread thread;

    public EvolutionWindow(AbstractWorldMap map, int startAnimalsNum, int startPlantsNum, int dailyGrown) {
        this.mapa = map;
        System.out.println("map1 " + Integer.toString(mapa.listOfAnimals.size()));
        this.engine = new SimulationEngine(mapa, startAnimalsNum, startPlantsNum, dailyGrown, this);
        System.out.println("map2 " + Integer.toString(mapa.listOfAnimals.size()));
        this.stage = new Stage();
        this.buttons = new AppButtons(engine);
        this.boxWithButtons = buttons.getBox();
        this.statisticsReport = new StatisticsReport(mapa);
        this.thread = new Thread(engine);
        this.startAnimalsNum = startAnimalsNum;
        System.out.println("dupa2" + Integer.toString(startAnimalsNum));
    }

    public void updateCharts() {
        animalsNumber.handler(1, mapa);
        plantsNumber.handler(2, mapa);
        freeFields.handler(3, mapa);
        avgEnergy.handler(4, mapa);
        avgLifeLength.handler(5, mapa);
    }

    public void drawGame() throws FileNotFoundException{
        System.out.println("map3 " + Integer.toString(mapa.listOfAnimals.size()));
        updateCharts();
        statisticsReport.updateStatistics();
        drawMap();
        scene = new Scene(mainbox, 2000,1000);
        scene.setRoot(mainbox);
        stage.setScene(scene);

        stage.show();
        System.out.println("dupa4" + Integer.toString(startAnimalsNum));
    }



    public void drawMap() {
        System.out.println("map4 " + Integer.toString(mapa.listOfAnimals.size()));
        System.out.println("dupa5"  + Integer.toString(startAnimalsNum));
        System.out.println("dziala3"  + Integer.toString(startAnimalsNum));
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(false);
        System.out.println("dupa5"  + Integer.toString(startAnimalsNum));
        System.out.println("dziala3"  + Integer.toString(startAnimalsNum));
        for (int i = mapa.low.x; i <= mapa.high.x; i++){
            Label numberX = new Label("");
            gridPane.add(numberX,  i - mapa.low.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            GridPane.setHalignment(numberX, HPos.CENTER);
        }

        for (int i = mapa.low.y; i <= mapa.high.y; i++){

            Label numberY = new Label("");
            gridPane.add(numberY, 0,mapa.high.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            GridPane.setHalignment(numberY, HPos.CENTER);
        }

        System.out.println(Integer.toString(mapa.listOfAnimals.size()));
        for (Animal element: mapa.listOfAnimals){
            System.out.println("dupa9"  + Integer.toString(startAnimalsNum));
            System.out.println(Integer.toString(mapa.listOfAnimals.size()));
            VBox elem = new GuiElementBox(element).getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - mapa.low.x + 1, mapa.high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }

        for (Grass element : mapa.grasses.values()){

            VBox elem = new GuiElementBox(element).getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - mapa.low.x + 1, mapa.high.y - pos.y + 1);
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

    public Thread getThread() {
        return thread;
    }

    public Stage getStage() {
        return stage;
    }

}
