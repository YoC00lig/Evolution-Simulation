package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {
    private AbstractWorldMap map;
    SimulationEngine engine;
    private GridPane gridPane = new GridPane();
    private final BorderPane border = new BorderPane();
    Stage stage;
    Scene scene;
    final int size = 25; // rozmiar mapy
    private final LineCharts animalsNumber = new LineCharts("Animals number", "Animals");
    private final LineCharts plantsNumber = new LineCharts("Plants number", "Plants");
    private final LineCharts avgEnergy = new LineCharts("Average animal energy", "Energy");
    private final LineCharts avgLifeLength = new LineCharts("Average life length", "Life length [days]");
    private final LineCharts freeFields = new LineCharts("Free fields on the map", "Free fields");
    AppButtons buttons;
    HBox boxWithButtons;
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

        TextField widthField = new TextField("15");
        TextField heightField = new TextField("15");

        ComboBox<String> predistinationMode = new ComboBox<>();
        predistinationMode.getItems().add("true");
        predistinationMode.getItems().add("false");

        ComboBox<String> toxicDeadMode = new ComboBox<>();
        toxicDeadMode.getItems().add("true");
        toxicDeadMode.getItems().add("false");

        ComboBox<String> isCrazyMode = new ComboBox<>();
        isCrazyMode.getItems().add("true");
        isCrazyMode.getItems().add("false");

        ComboBox<String> hellExistsMode = new ComboBox<>();
        hellExistsMode.getItems().add("true");
        hellExistsMode.getItems().add("false");

        TextField reproductionEnergy = new TextField("3");
        TextField plantEnergy = new TextField("2");
        TextField initialEnergy = new TextField("100");
        TextField startAnimalsNumber = new TextField("5");
        TextField startPlantsNumber = new TextField("1");
        TextField dailyGrownGrassNumber = new TextField("1");
        TextField numberOfGenes = new TextField("32");

        widthField.setStyle("-fx-background-color: #f7cac9"); widthField.setPrefColumnCount(14);
        heightField.setStyle("-fx-background-color: #f7cac9"); heightField.setPrefColumnCount(14);
        predistinationMode.setStyle("-fx-background-color: #f7cac9");
        toxicDeadMode.setStyle("-fx-background-color: #f7cac9");
        isCrazyMode.setStyle("-fx-background-color: #f7cac9");
        hellExistsMode.setStyle("-fx-background-color: #f7cac9");
        reproductionEnergy.setStyle("-fx-background-color: #f7cac9"); reproductionEnergy.setPrefColumnCount(14);
        plantEnergy.setStyle("-fx-background-color: #f7cac9"); plantEnergy.setPrefColumnCount(14);
        initialEnergy.setStyle("-fx-background-color: #f7cac9"); initialEnergy.setPrefColumnCount(14);
        startAnimalsNumber.setStyle("-fx-background-color: #f7cac9"); startAnimalsNumber.setPrefColumnCount(14);
        startPlantsNumber.setStyle("-fx-background-color: #f7cac9"); startPlantsNumber.setPrefColumnCount(14);
        dailyGrownGrassNumber.setStyle("-fx-background-color: #f7cac9"); dailyGrownGrassNumber.setPrefColumnCount(14);
        numberOfGenes.setStyle("-fx-background-color: #f7cac9"); numberOfGenes.setPrefColumnCount(14);

        listOfTextField.getChildren().addAll(widthField, heightField, predistinationMode, toxicDeadMode, isCrazyMode,
                hellExistsMode, reproductionEnergy, plantEnergy, initialEnergy, startAnimalsNumber,
                startPlantsNumber, dailyGrownGrassNumber, numberOfGenes);

        listOfTextField.setSpacing(13);

        VBox listOfLabel = new VBox();
        Label widthFieldLabel = new Label("Width: "); widthFieldLabel.setFont(new Font("Verdana", 14));
        Label heightFieldLabel = new Label("Height: "); heightFieldLabel.setFont(new Font("Verdana", 14));
        Label predistinationModeLabel = new Label("Predistination mode?"); predistinationModeLabel.setFont(new Font("Verdana", 14));
        Label toxicDeadModeLabel = new Label("Toxic-dead mode?"); toxicDeadModeLabel.setFont(new Font("Verdana", 14));
        Label isCrazyModeLabel = new Label("Is-crazy mode?"); isCrazyModeLabel.setFont(new Font("Verdana", 14));
        Label hellExistsModeLabel = new Label("hell's portal mode?"); hellExistsModeLabel.setFont(new Font("Verdana", 14));
        Label reproductionEnergyLabel = new Label("reproduction energy: "); reproductionEnergyLabel.setFont(new Font("Verdana", 14));
        Label plantEnergyLabel = new Label("plant energy: "); plantEnergyLabel.setFont(new Font("Verdana", 14));
        Label initialEnergyLabel = new Label("initial animal energy: "); initialEnergyLabel.setFont(new Font("Verdana", 14));
        Label startAnimalsNumberLabel = new Label("start number of animals: "); startAnimalsNumberLabel.setFont(new Font("Verdana", 14));
        Label startPlantsNumberLabel = new Label("start number of plants: "); startPlantsNumberLabel.setFont(new Font("Verdana", 14));
        Label dailyGrownGrassNumberLabel = new Label("number of plants per-day:       "); dailyGrownGrassNumberLabel.setFont(new Font("Verdana", 14));
        Label numberOfGenesLabel = new Label("Length of genotype: "); numberOfGenesLabel.setFont(new Font("Verdana", 14));

        Button confirmButton = new Button("CONFIRM");
        confirmButton.setStyle("-fx-background-color: #ff6666");

        listOfLabel.getChildren().addAll(widthFieldLabel, heightFieldLabel, predistinationModeLabel, toxicDeadModeLabel, isCrazyModeLabel,
                hellExistsModeLabel, reproductionEnergyLabel, plantEnergyLabel, initialEnergyLabel,  startAnimalsNumberLabel,
                startPlantsNumberLabel, dailyGrownGrassNumberLabel, numberOfGenesLabel, confirmButton);

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

            confirmButton.setEffect(new DropShadow());

            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            boolean predisitination = Boolean.parseBoolean(String.valueOf(predistinationMode));
            boolean toxicDead = Boolean.parseBoolean(String.valueOf(toxicDeadMode));
            boolean isCrazy = Boolean.parseBoolean(String.valueOf(isCrazyMode));
            boolean hellExists = Boolean.parseBoolean(String.valueOf(isCrazyMode));
            int reproductionE = Integer.parseInt(reproductionEnergy.getText());
            int plantE = Integer.parseInt(plantEnergy.getText());
            int initialE = Integer.parseInt(initialEnergy.getText());
            int startAnimalsNum = Integer.parseInt(startAnimalsNumber.getText());
            int startPlantsNum = Integer.parseInt(startPlantsNumber.getText());
            int dailyGrown = Integer.parseInt(dailyGrownGrassNumber.getText());
            int NumberOfGenes = Integer.parseInt(numberOfGenes.getText());


            if (toxicDead) map = new ToxicMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);
            else map = new EquatorMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);

            engine = new SimulationEngine(map, startAnimalsNum, startPlantsNum, dailyGrown, this);

            statisticsReport = new StatisticsReport(map);
            buttons = new AppButtons(engine);
            boxWithButtons = buttons.getBox();

            Thread thread = new Thread(engine);
            thread.start();
        });

        scene = new Scene(border, 2000,1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateCharts() {
        animalsNumber.handler(1, map);
        plantsNumber.handler(2, map);
        freeFields.handler(3, map);
        avgEnergy.handler(4, map);
        avgLifeLength.handler(5, map);
    }

    public void drawGame() throws FileNotFoundException{
        updateCharts();
        statisticsReport.updateStatistics();
        engine.run();
        drawMap();
        scene.setRoot(mainbox);
        stage.setScene(scene);
        stage.show();
    }

    public void drawMap() {
        gridPane.getChildren().clear();
        gridPane = new GridPane();
        Label label = new Label("");

        gridPane.add(label, 0, 0);
        gridPane.getRowConstraints().add(new RowConstraints(size));
        gridPane.getColumnConstraints().add(new ColumnConstraints(size));
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.setGridLinesVisible(true);

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
        Button saveButton = buttons.getSaveButton();
        stats.getChildren().add(saveButton);
        VBox StatsButtons = new VBox(stats, boxWithButtons);

        mainbox = new HBox(gridPane, charts, stats,  StatsButtons);
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
