package agh.ics.oop.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameOver {
    private final BorderPane border = new BorderPane();
    Scene scene;

    public void create() {
        Label title = new Label("All the animals have already died");
        title.setStyle("-fx-font-weight: bold");
        title.setFont(new Font(60));
        title.setAlignment(Pos.CENTER);
        border.setStyle("-fx-background-color: #eea29a;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(40,0,40,0));
        border.setTop(title);

        Image image = null;
        try {
            image = new Image(new FileInputStream("src/main/resources/grave.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ImageView view = new ImageView(image);
        BorderPane.setAlignment(view, Pos.CENTER);
        border.setCenter(view);

        Button exitButton = new Button("EXIT");


        styleButtonHover(exitButton);
        exitButton.setOnAction(event -> ((Stage) (((Button) event.getSource()).getScene().getWindow())).close());

        exitButton.setStyle("-fx-background-color: #ff6666");

        BorderPane.setAlignment(exitButton, Pos.CENTER);

        exitButton.setMinWidth(80);
        styleButtonHover(exitButton);

        HBox box = new HBox(exitButton);
        box.setPrefWidth(2000);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        BorderPane.setMargin(box, new Insets(40,0,100,0));
        border.setBottom(box);
        scene = new Scene(border, 2000, 1000);
    }


    public Scene getScene() {
        create();
        return scene;
    }

    public void styleButtonHover(Button B) {
        B.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> B.setEffect(new DropShadow()));
        B.addEventHandler(MouseEvent.MOUSE_EXITED, e -> B.setEffect(null));
    }


}
