package org.example.ooppr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerOptionsController {

    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public Button ButtonJoinServer;

    @FXML
    public Button ButtonHostServer;

    /**
     * Switching to CreationInterface
     */
    public void SwitchToJoin(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreationInterfaceV2.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        // getting controller, canvas sizing
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
