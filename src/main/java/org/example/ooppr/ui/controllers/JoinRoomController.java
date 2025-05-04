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
        String ipAddr;
        int port;
        try {
            String ipAddress = ipHolder.getText();
            if (ipAddress == null || ipAddress.isEmpty()) {
                showAlert("IP error", "No IP entered", "Please enter the server IP address.");
                return;
            } else {
                String[] parts = ipAddress.split(":");
                ipAddr = parts[0];
                port = Integer.parseInt(parts[1]);
            }

            // Connect to server with callback
            Client.connect(ipAddr, port, new Client.CanvasParamsCallback() { //WARN REMOVE MOCK HARDCODE
                @Override
                public void onCanvasParamsReceived(int xRes, int yRes, Color color) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/Product.fxml"));
                            Parent root = loader.load();
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //WARN RECHECK type->type

                            ProductController controller = loader.getController();
                            controller.initializeCanvas(xRes, yRes, color);
                            controller.initializeCanvasByHistory();

                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                public void onConnectionError(String message) {
                    Platform.runLater(() ->
                            showAlert("Connection Error", "Connection Failed", message));
                }
            });
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
