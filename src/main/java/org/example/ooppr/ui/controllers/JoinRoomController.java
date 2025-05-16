package org.example.ooppr.ui.controllers;

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
import org.example.ooppr.core.network.Client;
import org.example.ooppr.core.users.User;
import org.example.ooppr.core.users.User.Role;

import java.io.IOException;

public class JoinRoomController {

    public TextField Nickname;
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public TextField XResolutionHolder;

    @FXML
    public TextField YResolutionHolder;

    @FXML
    public ColorPicker StandartCanvasColorPicker;

    @FXML
    public TextField ipHolder;

    /**
     * The method switches the scene to Product. Checks the sizes of the passed canvas XResolutionHolder and YResolutionHolder.
     */
    public void SwitchToPaintPanel(ActionEvent event) throws IOException {
        String ip;
        int port;
        String nickname = "guest"; // TODO nickname dynamic choosing
        User user = new User( nickname, Role.VIEW_ONLY );
        try { // Get ip address and port by user input
            String ipAddress = ipHolder.getText();
            if (ipAddress == null || ipAddress.isEmpty()) {
                showAlert("IP error", "No IP entered", "Please enter the server IP address.");
                return;
            } else {
                String[] parts = ipAddress.split(":");
                ip = parts[0];
                port = Integer.parseInt(parts[1]);
                if( port >= 65535 ) {
                    showAlert( "Port value error", "Invalid port", "Please input valid port value" );
                    return;
                }
            };

            // Connect to server
            // Open local paint panel as Client
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/Product.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ProductController productController = loader.getController();
            productController.setIpPort( ip, port );
            productController.connectToHost( ip, port, user );

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


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
