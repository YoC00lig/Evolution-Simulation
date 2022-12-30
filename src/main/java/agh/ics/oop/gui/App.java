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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {

    private final BorderPane border = new BorderPane();
    private Stage stage;
    private Scene scene;

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
        TextField numberOfGenes = new TextField("32");

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
            int NumberOfGenes = Integer.parseInt(numberOfGenes.getText());

            AbstractWorldMap map;

            if (toxicDead) map = new ToxicMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);
            else map = new EquatorMap(width, height, predisitination, isCrazy, hellExists, reproductionE, plantE, initialE, NumberOfGenes);

            EvolutionWindow simulation = new EvolutionWindow();
            simulation.initStartScene(map, startAnimalsNum, startPlantsNum, dailyGrown, this);
        });

        scene = new Scene(border, 2000,1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
