package it.unicam.cs.pa.jbudget.javaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXJBudget extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/JBudget.fxml"));

        stage.setTitle("JBudget");
        Image ico = new Image("/JBudget_Icon.png");
        stage.getIcons().add(ico);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
