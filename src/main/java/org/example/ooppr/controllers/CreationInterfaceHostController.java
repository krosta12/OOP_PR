package org.example.ooppr.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.ooppr.Server.Client;
import org.example.ooppr.Server.Server;

import java.io.IOException;

public class CreationInterfaceHostController {

    public TextField portHolder;
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public TextField XResolutionHolder;

    @FXML
    public TextField YResolutionHolder;

    @FXML
    public ColorPicker StandartCanvasColorPicker;

    /**
     * The method switches the scene to Product. Checks the sizes of the passed canvas XResolutionHolder and YResolutionHolder.
     */
    public void SwitchToPaintPanel(ActionEvent event) throws IOException {
        try { // CREATING HOST ROOM

            int XResolution = Integer.parseInt(XResolutionHolder.getText());
            int YResolution = Integer.parseInt(YResolutionHolder.getText());

            // Getting canvas default color
            Color defaultColor = StandartCanvasColorPicker.getValue();

            if (resolutionIsValid(XResolution, YResolution)) {
                // Create server with canvas parameters
                // checking given by user port
                int port = Integer.parseInt( portHolder.getText() );
                final Server server = new Server(port, XResolution, YResolution, defaultColor); //1234 - REMOVE HARDCODE WARN
                new Thread(server::startHost).start(); // Start a server on new Thread WARN CHECK A MEMORY LEAK AFTER new

                // Open local paint panel as Client
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/Product.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                ProductController productController = loader.getController();
                productController.initializeCanvas(XResolution, YResolution, defaultColor);

                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert("Resolution error", "Wrong resolution!", "Please enter a valid resolution (positive integer).");
            }
        } catch (NumberFormatException e) {
            showAlert("Input error", "Invalid input!", "Please input integer values.");
        }
    }

    /**
     * The method checks the validity of the canvas size
     * @param xResolution canvas x resolution (width)
     * @param yResolution canvas y resolution (height)
     * @return true if valid, false if not
     */
    private boolean resolutionIsValid(int xResolution, int yResolution) {
        return xResolution > 0 && yResolution > 0;
    }

    /**
     * The method shows alerts
     * @param title alert title text
     * @param header alert header text
     * @param content alert content text
     */
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}