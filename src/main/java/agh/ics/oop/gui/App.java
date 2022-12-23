package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.geometry.HPos;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import agh.ics.oop.*;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.application.Platform;
import java.io.FileNotFoundException;
import javafx.scene.layout.VBox;

public class App extends Application {
    private AbstractWorldMap map;
    SimulationEngine engine;
    private GridPane gridPane = new GridPane();
    private final BorderPane border = new BorderPane();
    Stage stage;
    Scene scene;
    final int size = 25; // rozmiar mapy
    private final LineCharts animalsNumber = new LineCharts("Animals number");
    private final LineCharts plantsNumber = new LineCharts("Plants number");
    private final LineCharts avgEnergy = new LineCharts("Average animal energy");
    private final LineCharts avgLifeLength = new LineCharts("Average life length");
    private final LineCharts freeFields = new LineCharts("Free fields on the map");
    private StatisticsReport  statisticsReport;

    HBox mainbox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Label title = new Label("Input your own parameters: ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(40));
        title.setAlignment(Pos.CENTER);

        VBox listOfTextField = new VBox();

        TextField widthField = new TextField("25");
        TextField heightField = new TextField("25");
        TextField predistinationMode = new TextField("true");
        TextField toxicDeadMode = new TextField("false");
        TextField isCrazyMode = new TextField("true");
        TextField hellExistsMode = new TextField("true");
        TextField reproductionEnergy = new TextField("3");
        TextField plantEnergy = new TextField("2");
        TextField initialEnergy = new TextField("100");
        TextField startAnimalsNumber = new TextField("20");
        TextField startPlantsNumber = new TextField("1");
        TextField dailyGrownGrassNumber = new TextField("1");

        widthField.setStyle("-fx-background-color: #f7cac9"); widthField.setPrefColumnCount(14);
        heightField.setStyle("-fx-background-color: #f7cac9"); heightField.setPrefColumnCount(14);
        predistinationMode.setStyle("-fx-background-color: #f7cac9"); predistinationMode.setPrefColumnCount(14);
        toxicDeadMode.setStyle("-fx-background-color: #f7cac9"); toxicDeadMode.setPrefColumnCount(14);
        isCrazyMode.setStyle("-fx-background-color: #f7cac9"); isCrazyMode.setPrefColumnCount(14);
        hellExistsMode.setStyle("-fx-background-color: #f7cac9"); hellExistsMode.setPrefColumnCount(14);
        reproductionEnergy.setStyle("-fx-background-color: #f7cac9"); reproductionEnergy.setPrefColumnCount(14);
        plantEnergy.setStyle("-fx-background-color: #f7cac9"); plantEnergy.setPrefColumnCount(14);
        initialEnergy.setStyle("-fx-background-color: #f7cac9"); initialEnergy.setPrefColumnCount(14);
        startAnimalsNumber.setStyle("-fx-background-color: #f7cac9"); startAnimalsNumber.setPrefColumnCount(14);
        startPlantsNumber.setStyle("-fx-background-color: #f7cac9"); startPlantsNumber.setPrefColumnCount(14);
        dailyGrownGrassNumber.setStyle("-fx-background-color: #f7cac9"); dailyGrownGrassNumber.setPrefColumnCount(14);

        listOfTextField.getChildren().addAll(widthField, heightField, predistinationMode, toxicDeadMode, isCrazyMode,
                hellExistsMode, reproductionEnergy, plantEnergy, initialEnergy, startAnimalsNumber,
                startPlantsNumber, dailyGrownGrassNumber);

        listOfTextField.setSpacing(13);

        VBox listOfLabel = new VBox();
        Label widthFieldLabel = new Label("map width: "); widthFieldLabel.setFont(new Font("Verdana", 14));
        Label heightFieldLabel = new Label("map height: "); heightFieldLabel.setFont(new Font("Verdana", 14));
        Label predistinationModeLabel = new Label("Predistination mode on?"); predistinationModeLabel.setFont(new Font("Verdana", 14));
        Label toxicDeadModeLabel = new Label("Toxic-dead mode on?"); toxicDeadModeLabel.setFont(new Font("Verdana", 14));
        Label isCrazyModeLabel = new Label("Is-crazy mode on?"); isCrazyModeLabel.setFont(new Font("Verdana", 14));
        Label hellExistsModeLabel = new Label("hell's portal mode on?"); hellExistsModeLabel.setFont(new Font("Verdana", 14));
        Label reproductionEnergyLabel = new Label("reproduction energy: "); reproductionEnergyLabel.setFont(new Font("Verdana", 14));
        Label plantEnergyLabel = new Label("plant energy: "); plantEnergyLabel.setFont(new Font("Verdana", 14));
        Label initialEnergyLabel = new Label("initial energy for animal: "); initialEnergyLabel.setFont(new Font("Verdana", 14));
        Label startAnimalsNumberLabel = new Label("start number of animals: "); startAnimalsNumberLabel.setFont(new Font("Verdana", 14));
        Label startPlantsNumberLabel = new Label("start number of plants: "); startPlantsNumberLabel.setFont(new Font("Verdana", 14));
        Label dailyGrownGrassNumberLabel = new Label("number of plants per-day:       "); dailyGrownGrassNumberLabel.setFont(new Font("Verdana", 14));


        Button confirmButton = new Button("CONFIRM");
        confirmButton.setStyle("-fx-background-color: #ff6666");

        listOfLabel.getChildren().addAll(widthFieldLabel, heightFieldLabel, predistinationModeLabel, toxicDeadModeLabel, isCrazyModeLabel,
                hellExistsModeLabel, reproductionEnergyLabel, plantEnergyLabel, initialEnergyLabel,  startAnimalsNumberLabel,
                startPlantsNumberLabel, dailyGrownGrassNumberLabel, confirmButton);
        listOfLabel.setSpacing(20);

        HBox inputList = new HBox();
        inputList.getChildren().addAll(listOfLabel, listOfTextField);
        inputList.setAlignment(Pos.TOP_CENTER);

        border.setStyle("-fx-background-color: #eea29a;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(40,0,40,0));
        border.setTop(title);
        border.setCenter(inputList);

        confirmButton.setOnAction( event -> {

            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            boolean predisitination = Boolean.parseBoolean(predistinationMode.getText());
            boolean toxicDead = Boolean.parseBoolean(toxicDeadMode.getText());
            boolean isCrazy = Boolean.parseBoolean(isCrazyMode.getText());
            boolean hellExists = Boolean.parseBoolean(isCrazyMode.getText());
            int reproductionE = Integer.parseInt(reproductionEnergy.getText());
            int plantE = Integer.parseInt(plantEnergy.getText());
            int initialE = Integer.parseInt(initialEnergy.getText());
            int startAnimalsNum = Integer.parseInt(startAnimalsNumber.getText());
            int startPlantsNum = Integer.parseInt(startPlantsNumber.getText());
            int dailyGrown = Integer.parseInt(dailyGrownGrassNumber.getText());


            if (toxicDead) map = new ToxicMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE);
            else map = new EquatorMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE);

            engine = new SimulationEngine(map, startAnimalsNum, startPlantsNum, dailyGrown, this);

            statisticsReport = new StatisticsReport(map);

            Thread thread = new Thread(engine);
            thread.start();
        });

        scene = new Scene(border, 2000,1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawGame() throws FileNotFoundException{
        animalsNumber.updateAnimalsNumber(map);
        plantsNumber.updatePlantsNumber(map);
        freeFields.updateFreeFields(map);
        avgEnergy.updateEnergy(map);
        avgLifeLength.updateEnergy(map);
        statisticsReport.updateStatistics(map);
        engine.run();
        drawMap();
        scene.setRoot(mainbox);
        stage.setScene(scene);
        stage.show();
    }

    public void drawMap() {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("y/x");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(true);

        for (int i = map.low.x; i <= map.high.x; i++){
            Label numberX = new Label("" + i );
            gridPane.add(numberX,  i - map.low.x + 1, 0);
            gridPane.getColumnConstraints().add(new ColumnConstraints(size));
            GridPane.setHalignment(numberX, HPos.CENTER);
        }

        for (int i = map.low.y; i <= map.high.y; i++){
            Label numberY = new Label("" + i);
            gridPane.add(numberY, 0,map.high.y - i + 1);
            gridPane.getRowConstraints().add(new RowConstraints(size));
            GridPane.setHalignment(numberY, HPos.CENTER);
        }

        for (Animal element: map.listOfAnimals){
            VBox elem = new GuiElementBox(element).getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - map.low.x + 1, map.high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }

        for (Grass element : map.grasses.values()){
            VBox elem = new GuiElementBox(element).getvBox();
            Vector2d pos = element.getPosition();
            gridPane.add(elem,  pos.x - map.low.x + 1, map.high.y - pos.y + 1);
            GridPane.setHalignment(elem, HPos.CENTER);
        }
        gridPane.setStyle("-fx-background-color: #eea29a;");
        gridPane.setAlignment(Pos.CENTER_LEFT);
        VBox charts = new VBox(animalsNumber.getChart(), plantsNumber.getChart(), freeFields.getChart(),
                avgEnergy.getChart(), avgLifeLength.getChart());
        charts.setAlignment(Pos.CENTER);
        VBox stats = statisticsReport.getStatistics();
        mainbox = new HBox(gridPane, charts, stats);
        HBox.setMargin(stats, new Insets(0,0,0,50));
        mainbox.setAlignment(Pos.CENTER);
        mainbox.setStyle("-fx-background-color: #eea29a;");
    }

    public void draw() throws FileNotFoundException{
        Platform.runLater(() -> {
            try {
                drawGame();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
