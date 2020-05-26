package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.setProperty("org.graphstream.ui", "javafx");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
        Parent root = loader.load();
        ((Controller)loader.getController()).setRoot((AnchorPane)root);
        primaryStage.setTitle("SFG Solver");
        primaryStage.setScene(new Scene(root, 791, 561));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
