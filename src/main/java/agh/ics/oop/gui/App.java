package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class App extends Application {

    private final BorderPane border = new BorderPane();
    Button confirmButton = new Button("CONFIRM");
    private Scene scene;
    private Map<IEngine, EvolutionWindow> windows = new HashMap<>();
    private final AbstractWorldMap[] exampleMaps = {new EquatorMap(10, 10, false, false, true, 2, 1, 20, 20, 1, 7, 2),
            new ToxicMap(18, 18, true, false, true, 3, 2, 30, 12, 4, 5, 3)};
    private final SimulationEngine[] exampleEngines = {new SimulationEngine(exampleMaps[0], 5, 2, 2, this), new SimulationEngine(exampleMaps[1], 15, 1, 1, this)};
    List<Thread> threads = new LinkedList<>();
    private boolean isdefault = false;
    private int canAgain = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StartScene startscene = new StartScene();
        Button startButton = startscene.getStartButton();
        startButton.setOnAction(event -> {
            initStartScene();
            scene = new Scene(border, 2000, 1000);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
        Scene sceneStart = startscene.getScene();
        primaryStage.setScene(sceneStart);
        primaryStage.show();

    }
    public void initStartScene() {
        Label title = new Label("Input your own parameters: ");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(60));
        title.setAlignment(Pos.CENTER);

        // textFields and ComboBoxes (for choosing options)
        TextField widthField = new TextField("15"); widthField.setStyle("-fx-background-color: #f7cac9");
        TextField heightField = new TextField("15"); heightField.setStyle("-fx-background-color: #f7cac9");

        ComboBox<String> SaveToFile = new ComboBox<>();
        SaveToFile.getItems().add("true");
        SaveToFile.getItems().add("false");
        SaveToFile.setStyle("-fx-background-color: #f7cac9");

        ComboBox<String> predistinationMode = new ComboBox<>();
        predistinationMode.getItems().add("true");
        predistinationMode.getItems().add("false");
        predistinationMode.setStyle("-fx-background-color: #f7cac9");

        ComboBox<String> toxicDeadMode = new ComboBox<>();
        toxicDeadMode.getItems().add("true");
        toxicDeadMode.getItems().add("false");
        toxicDeadMode.setStyle("-fx-background-color: #f7cac9");

        ComboBox<String> isCrazyMode = new ComboBox<>();
        isCrazyMode.getItems().add("true");
        isCrazyMode.getItems().add("false");
        isCrazyMode.setStyle("-fx-background-color: #f7cac9");

        ComboBox<String> hellExistsMode = new ComboBox<>();
        hellExistsMode.getItems().add("true");
        hellExistsMode.getItems().add("false");
        hellExistsMode.setStyle("-fx-background-color: #f7cac9");

        TextField reproductionEnergy = new TextField("3"); reproductionEnergy.setStyle("-fx-background-color: #f7cac9");
        TextField plantEnergy = new TextField("2"); plantEnergy.setStyle("-fx-background-color: #f7cac9");
        TextField initialEnergy = new TextField("30");  initialEnergy.setStyle("-fx-background-color: #f7cac9");
        TextField startAnimalsNumber = new TextField("5"); startAnimalsNumber.setStyle("-fx-background-color: #f7cac9");
        TextField startPlantsNumber = new TextField("1"); startPlantsNumber.setStyle("-fx-background-color: #f7cac9");
        TextField dailyGrownGrassNumber = new TextField("1"); dailyGrownGrassNumber.setStyle("-fx-background-color: #f7cac9");
        TextField numberOfGenes = new TextField("32"); numberOfGenes.setStyle("-fx-background-color: #f7cac9");
        TextField minMutations = new TextField("3");  minMutations.setStyle("-fx-background-color: #f7cac9");
        TextField maxMutations = new TextField("6"); maxMutations.setStyle("-fx-background-color: #f7cac9");
        TextField reproductionCost = new TextField("2"); reproductionCost.setStyle("-fx-background-color: #f7cac9");


        // Labels
        Label SaveToFileLabel = new Label("Save data?: ");
        Label widthFieldLabel = new Label("Width (5-20): ");
        Label heightFieldLabel = new Label("Height (5-20): ");
        Label predistinationModeLabel = new Label("Predistination mode?");
        Label toxicDeadModeLabel = new Label("Toxic-dead mode?");
        Label isCrazyModeLabel = new Label("Is-crazy mode?");
        Label hellExistsModeLabel = new Label("hell's portal mode?");
        Label reproductionEnergyLabel = new Label("reproduction energy (2-5): ");
        Label plantEnergyLabel = new Label("plant energy (1-5): ");
        Label initialEnergyLabel = new Label("initial animal energy (10-50): ");
        Label startAnimalsNumberLabel = new Label("start number of animals (1-20): ");
        Label startPlantsNumberLabel = new Label("start number of plants (1-10): ");
        Label dailyGrownGrassNumberLabel = new Label("number of plants per-day (1-3): ");
        Label numberOfGenesLabel = new Label("Length of genotype (5-32): ");
        Label minMutationsLabel = new Label("Minimum number of mutations (0 - maximum number of mutations): ");
        Label maxMutationsLabel = new Label("Maximum number of mutations (minimum number of mutations - length of genotype): ");
        Label reproductionCostLabel = new Label("Energy that animal lost in reproduction: ");

        setFonts(new Label[]{SaveToFileLabel, widthFieldLabel, heightFieldLabel, predistinationModeLabel,
                toxicDeadModeLabel, isCrazyModeLabel, hellExistsModeLabel,
                reproductionEnergyLabel, plantEnergyLabel, initialEnergyLabel, startAnimalsNumberLabel,
                startPlantsNumberLabel, dailyGrownGrassNumberLabel, numberOfGenesLabel, minMutationsLabel, maxMutationsLabel, reproductionCostLabel});


        // labels with textFields - input list

        VBox TextFields = new VBox();
        TextFields.setSpacing(13);

        VBox listOfLabel = new VBox();
        listOfLabel.setSpacing(20);

        TextFields.getChildren().addAll(SaveToFile, widthField, heightField, predistinationMode, toxicDeadMode, isCrazyMode,
                hellExistsMode, reproductionEnergy, plantEnergy, initialEnergy, startAnimalsNumber,
                startPlantsNumber, dailyGrownGrassNumber, numberOfGenes, minMutations, maxMutations, reproductionCost);

        listOfLabel.getChildren().addAll(SaveToFileLabel, widthFieldLabel, heightFieldLabel, predistinationModeLabel, toxicDeadModeLabel, isCrazyModeLabel,
                hellExistsModeLabel, reproductionEnergyLabel, plantEnergyLabel, initialEnergyLabel,  startAnimalsNumberLabel,
                startPlantsNumberLabel, dailyGrownGrassNumberLabel, numberOfGenesLabel, minMutationsLabel, maxMutationsLabel, reproductionCostLabel);

        HBox inputList = new HBox();
        inputList.getChildren().addAll(listOfLabel, TextFields);
        inputList.setAlignment(Pos.TOP_CENTER);

        // buttons

        Button playButton = new Button("PLAY");
        playButton.setStyle("-fx-background-color: #ff6666");
        styleButtonHover(playButton);

        Button defaultButton = new Button("DEFAULT SIMULATION");
        defaultButton.setStyle("-fx-background-color: #ff6666");
        styleButtonHover(defaultButton);

        styleButtonHover(confirmButton);
        confirmButton.setStyle("-fx-background-color: #ff6666");

        HBox startButtons = new HBox(confirmButton, playButton, defaultButton);
        startButtons.setSpacing(20);
        startButtons.setAlignment(Pos.CENTER);

        defaultButton.setOnAction(event -> this.isdefault = true);

        confirmButton.setOnAction( event -> {
            AbstractWorldMap map;
            SimulationEngine newEngine;
            boolean saveCSV;
            if (!isdefault) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                saveCSV = Boolean.parseBoolean(SaveToFile.getValue());
                boolean predisitination = Boolean.parseBoolean(predistinationMode.getValue());
                boolean toxicDead = Boolean.parseBoolean(toxicDeadMode.getValue());
                boolean isCrazy = Boolean.parseBoolean(isCrazyMode.getValue());
                boolean hellExists = Boolean.parseBoolean(hellExistsMode.getValue());
                int reproductionE = Integer.parseInt(reproductionEnergy.getText());
                int plantE = Integer.parseInt(plantEnergy.getText());
                int initialE = Integer.parseInt(initialEnergy.getText());
                int startAnimalsNum = Integer.parseInt(startAnimalsNumber.getText());
                int startPlantsNum = Integer.parseInt(startPlantsNumber.getText());
                int dailyGrown = Integer.parseInt(dailyGrownGrassNumber.getText());
                int NumberOfGenes = Integer.parseInt(numberOfGenes.getText());
                int minMut = Integer.parseInt(minMutations.getText());
                int maxMut = Integer.parseInt(maxMutations.getText());
                int reprCost = Integer.parseInt(reproductionCost.getText());

                checkArguments(width, height, startAnimalsNum, startPlantsNum, dailyGrown, initialE, plantE, reproductionE, NumberOfGenes, minMut, maxMut, NumberOfGenes);



                if (toxicDead)
                    map = new ToxicMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes, minMut, maxMut, reprCost);
                else
                    map = new EquatorMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes, minMut, maxMut, reprCost);

                newEngine = new SimulationEngine(map, startAnimalsNum, startPlantsNum, dailyGrown, this);
            }
            else {
                Random random = new Random();
                int ind = random.nextInt(2);
                if (ind == 1){
                    map = new EquatorMap(10, 10, false, false, true, 2, 1, 20, 20, 1, 7, 2);
                    newEngine = new SimulationEngine(map, 5, 2, 2, this);
                }
                else {
                    map = new ToxicMap(18, 18, true, false, true, 3, 2, 30, 12, 4, 5, 3);
                    newEngine =  new SimulationEngine(map, 15, 1, 1, this);
                }
                saveCSV = true;
                isdefault = false;
            }

            Thread newThread = new Thread(newEngine);
            EvolutionWindow newSimulation = new EvolutionWindow(map, newEngine, newThread, saveCSV);
            threads.add(newThread);
            windows.put(newEngine, newSimulation);

        });

        playButton.setOnAction(event -> {
            for (Thread thread : threads) {
                thread.start();
                canAgain += 1;
            }
            confirmButton.setDisable(true);
        });

        // border
        border.setStyle("-fx-background-color: #eea29a;");
        border.setTop(title);
        border.setCenter(inputList);
        border.setBottom(startButtons);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20,0,10,0));
        BorderPane.setMargin(startButtons, new Insets(0,0,220,0));

    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
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
            Thread.currentThread().interrupt();
        }
    }

    public void setFonts(Label[] labels){
        for (Label label: labels) label.setFont(new Font("Verdana", 14));
    }

    public void checkArguments(int width, int height, int animalsNum, int plantsNum, int plantsPerDay, int initE, int plantE, int reprE, int genotypeLeng, int minMut, int maxMut, int numberOfGenes) {
        if (4 >= width || width > 20) throw new IllegalArgumentException("Width out of range");
        if (4 >= height || height > 20) throw new IllegalArgumentException("Height out of range");
        if (0 >= animalsNum || animalsNum > 20) throw new IllegalArgumentException("Start number of animals out of range");
        if (0 >= plantsNum || plantsNum > 10) throw new IllegalArgumentException("Start number of plants out of range");
        if (0 >= plantsPerDay || plantsPerDay > 3) throw new IllegalArgumentException("Plants-per-day number out of range");
        if (10 > initE || initE > 50) throw new IllegalArgumentException("Initial energy for animal number out of range");
        if (1 > plantE || plantE > 5) throw new IllegalArgumentException("Plant energy number out of range");
        if (2 > reprE || reprE > 5) throw new IllegalArgumentException("Reproduction energy number out of range");
        if (5 > genotypeLeng || genotypeLeng > 32) throw new IllegalArgumentException("Genotype length number out of range");
        if (minMut < 0 || maxMut < 0 || minMut > maxMut || maxMut > numberOfGenes)  throw new IllegalArgumentException("Mutations number out of range");
    }
    public void decrementCanAgain() {
        this.canAgain -= 1;
        if (canAgain == 0) {
            confirmButton.setDisable(false);
            threads = new LinkedList<>();
            windows = new HashMap<>();
        }
    }
}

