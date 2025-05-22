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
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;

public class JoinRoomController {

    @FXML
    public TextField nicknameHolder;
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

        try { // Get ip address and port by user input

            String nickname = nicknameHolder.getText().trim();
            String ipAddress = ipHolder.getText();

            // Checking all user input
            if (ipAddress == null || ipAddress.isEmpty())
                throw new IllegalArgumentException( "No IP entered" );

            if( nickname.isEmpty() )
                throw new IllegalArgumentException( "No nickname entered" );


            String[] parts = ipAddress.split(":");
            if( parts.length != 2 )
                throw new IllegalArgumentException( "IP must be in the format IP:PORT" );

            ip = parts[0];
            port = Integer.parseInt(parts[1]);
            if( port < 0 || port > 65525 )
                throw new IllegalArgumentException( "Port must be between 0 and 65535" );

            // Create user
            User user = new User( nickname, Role.VIEW_ONLY, LocalDateTime.now() );

            // Connect to server
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/Product.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ProductController productController = loader.getController();
            productController.setStage( stage );
            productController.setIpPort( ip, port );
            productController.connectToHost( ip, port, user );

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch ( NumberFormatException e ) {
            showAlert("Port value error", "Non-integer value", "Please input integer values.");
        } catch ( IllegalArgumentException e ) {
            showAlert( "Input error", e.getMessage(), "Please check your input." );
        } catch ( Exception e ) {
            showAlert( "Unknown error", "Something went wrong...\nDescription below: ", e.getMessage() );
        }
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
