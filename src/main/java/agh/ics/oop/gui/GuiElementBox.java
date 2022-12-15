package agh.ics.oop.gui;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import agh.ics.oop.*;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class GuiElementBox{
    private final VBox vBox;

    public GuiElementBox(IMapElement object){
        Image image = null;
        try {
            image = new Image(new FileInputStream(object.getPath(object)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        vBox = new VBox();
        vBox.getChildren().addAll(imageView);
        vBox.setAlignment(Pos.CENTER);
    }
    public VBox getvBox()  {
        return vBox;
    }

}