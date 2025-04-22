package org.example.ooppr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OPaintApplication extends Application {
    public static final String appName = "OPaint";
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(OPaintApplication.class.getResource("ServerOptions.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 350, 500);
        FXMLLoader fxmlLoader = new FXMLLoader(OPaintApplication.class.getResource("CreationInterfaceV2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 500);
        stage.setTitle(appName);
        stage.setScene(scene);
//        stage.setTitle(appName + " : Canvas creations");
//        stage.setResizable(false); for creation Panel
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}