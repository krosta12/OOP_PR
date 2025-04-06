package org.example.ooppr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("creationInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
//        stage.setTitle("OPaint : Canvas creations");
//        stage.setResizable(false); for creation Panel
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}