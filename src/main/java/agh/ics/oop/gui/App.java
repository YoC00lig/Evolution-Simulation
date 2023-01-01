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

public class App extends Application {

    private final BorderPane border = new BorderPane();
    private Scene scene;
    private Map<IEngine, EvolutionWindow> windows = new HashMap<>();
    List<Thread> threads = new LinkedList<>();
    Button confirmButton = new Button("CONFIRM");
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
        TextField initialEnergy = new TextField("30");
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
        Label widthFieldLabel = new Label("Width (5-20): "); widthFieldLabel.setFont(new Font("Verdana", 14));
        Label heightFieldLabel = new Label("Height (5-20): "); heightFieldLabel.setFont(new Font("Verdana", 14));
        Label predistinationModeLabel = new Label("Predistination mode?"); predistinationModeLabel.setFont(new Font("Verdana", 14));
        Label toxicDeadModeLabel = new Label("Toxic-dead mode?"); toxicDeadModeLabel.setFont(new Font("Verdana", 14));
        Label isCrazyModeLabel = new Label("Is-crazy mode?"); isCrazyModeLabel.setFont(new Font("Verdana", 14));
        Label hellExistsModeLabel = new Label("hell's portal mode?"); hellExistsModeLabel.setFont(new Font("Verdana", 14));
        Label reproductionEnergyLabel = new Label("reproduction energy (2-5): "); reproductionEnergyLabel.setFont(new Font("Verdana", 14));
        Label plantEnergyLabel = new Label("plant energy (1-5): "); plantEnergyLabel.setFont(new Font("Verdana", 14));
        Label initialEnergyLabel = new Label("initial animal energy (10-50): "); initialEnergyLabel.setFont(new Font("Verdana", 14));
        Label startAnimalsNumberLabel = new Label("start number of animals (1-20): "); startAnimalsNumberLabel.setFont(new Font("Verdana", 14));
        Label startPlantsNumberLabel = new Label("start number of plants (1-10): "); startPlantsNumberLabel.setFont(new Font("Verdana", 14));
        Label dailyGrownGrassNumberLabel = new Label("number of plants per-day (1-3):       "); dailyGrownGrassNumberLabel.setFont(new Font("Verdana", 14));
        Label numberOfGenesLabel = new Label("Length of genotype (5-32): "); numberOfGenesLabel.setFont(new Font("Verdana", 14));


        confirmButton.setStyle("-fx-background-color: #ff6666");
        Button playButton = new Button("PLAY");
        playButton.setStyle("-fx-background-color: #ff6666");

        listOfLabel.getChildren().addAll(widthFieldLabel, heightFieldLabel, predistinationModeLabel, toxicDeadModeLabel, isCrazyModeLabel,
                hellExistsModeLabel, reproductionEnergyLabel, plantEnergyLabel, initialEnergyLabel,  startAnimalsNumberLabel,
                startPlantsNumberLabel, dailyGrownGrassNumberLabel, numberOfGenesLabel, confirmButton, playButton);

        listOfLabel.setSpacing(20);

        HBox inputList = new HBox();
        inputList.getChildren().addAll(listOfLabel, listOfTextField);
        inputList.setAlignment(Pos.TOP_CENTER);

        border.setStyle("-fx-background-color: #eea29a;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(40,0,40,0));
        border.setTop(title);
        border.setCenter(inputList);

        styleButtonHover(confirmButton);
        styleButtonHover(playButton);

        confirmButton.setOnAction( event -> {

            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            boolean predisitination = Boolean.parseBoolean(String.valueOf(predistinationMode));
            boolean toxicDead = Boolean.parseBoolean(String.valueOf(toxicDeadMode));
            boolean isCrazy = Boolean.parseBoolean(String.valueOf(isCrazyMode));
            boolean hellExists = Boolean.parseBoolean(String.valueOf(hellExistsMode));
            int reproductionE = Integer.parseInt(reproductionEnergy.getText());
            int plantE = Integer.parseInt(plantEnergy.getText());
            int initialE = Integer.parseInt(initialEnergy.getText());
            int startAnimalsNum = Integer.parseInt(startAnimalsNumber.getText());
            int startPlantsNum = Integer.parseInt(startPlantsNumber.getText());
            int dailyGrown = Integer.parseInt(dailyGrownGrassNumber.getText());
            int NumberOfGenes = Integer.parseInt(numberOfGenes.getText());

            checkArguments(width, height, startAnimalsNum, startPlantsNum, dailyGrown, initialE, plantE, reproductionE, NumberOfGenes);

            AbstractWorldMap map;

            if (toxicDead) map = new ToxicMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);
            else map = new EquatorMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);

            SimulationEngine newEngine = new SimulationEngine(map, startAnimalsNum,  startPlantsNum, dailyGrown, this);
            Thread newThread = new Thread(newEngine);
            EvolutionWindow newSimulation = new EvolutionWindow(map, newEngine, newThread);
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

    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> B.setEffect(null));
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

    public void checkArguments(int width, int height, int animalsNum, int plantsNum, int plantsPerDay, int initE, int plantE, int reprE, int genotypeLeng) {
        if (0 >= width || width > 20) throw new IllegalArgumentException("Width out of range");
        if (0 >= height || height > 20) throw new IllegalArgumentException("Height out of range");
        if (0 >= animalsNum || animalsNum > 20) throw new IllegalArgumentException("Start number of animals out of range");
        if (0 >= plantsNum || plantsNum > 10) throw new IllegalArgumentException("Start number of plants out of range");
        if (0 >= plantsPerDay || plantsPerDay > 3) throw new IllegalArgumentException("Plants-per-day number out of range");
        if (10 >= initE || initE > 50) throw new IllegalArgumentException("Initial energy for animal number out of range");
        if (1 >= plantE || plantE > 5) throw new IllegalArgumentException("Plant energy number out of range");
        if (2 >= reprE || reprE > 5) throw new IllegalArgumentException("Reproduction energy number out of range");
        if (5 >= genotypeLeng || genotypeLeng > 32) throw new IllegalArgumentException("Genotype length number out of range");
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

