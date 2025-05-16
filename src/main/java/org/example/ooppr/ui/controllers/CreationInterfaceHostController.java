package org.example.ooppr.ui.controllers;

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
import org.example.ooppr.core.network.Server;
import org.example.ooppr.core.users.User;

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
    public TextField nicknameHolder;

    @FXML
    public ColorPicker StandartCanvasColorPicker;

    /**
     * The method switches the scene to Product. Checks the sizes of the passed canvas XResolutionHolder and YResolutionHolder.
     */
    public void SwitchToPaintPanel(ActionEvent event) throws IOException {
        try { // CREATING HOST ROOM

            // Data by user
            int XResolution = parsePositiveInt( XResolutionHolder.getText(), "XResolution");
            int YResolution = parsePositiveInt( YResolutionHolder.getText(), "YResolution" );
            int port = parsePort( portHolder.getText() );

            String nickname = nicknameHolder.getText().trim();
            if( nickname.isEmpty() )
                throw new IllegalArgumentException( "No nickname entered" );

            Color defaultColor = StandartCanvasColorPicker.getValue();// Getting canvas default color

            // Create user
            User creatorUser = new User( nickname, User.Role.CREATOR );

            // Create server
            final Server server = new Server(port, XResolution, YResolution, defaultColor);
            new Thread(server::startHost).start(); // Start a server on new Thread WARN CHECK A MEMORY LEAK AFTER new
            server.setCreator( creatorUser );

            // Open local paint panel as Client
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/Product.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            ProductController productController = loader.getController();
            productController.initializeCanvas(XResolution, YResolution, defaultColor);
            productController.setIpPort( server.getIp(), server.getPort() );
            productController.connectToHost( server.getIp(), server.getPort(), creatorUser );

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch ( NumberFormatException e ) {
            showAlert("Input error", "Non-integer value", "Please input integer values.");
        } catch ( IllegalArgumentException e ) {
            showAlert( "Input error", e.getMessage(), "Please check your input." );
        } catch ( Exception e ) {
            showAlert( "Unknown error", "Something went wrong...\nDescription below: ", e.getMessage() );
        }
    }

    /**
     * Converting text and checking it for positive
     */
    private int parsePositiveInt( String text, String fieldName ) {
        int value = Integer.parseInt( text );
        if( value <= 0 )
            throw new IllegalArgumentException( fieldName + " must be a positive number." );
        return value;
    }

    /**
     * Controlling port for valid value (between 0 and 65535)
     */
    private int parsePort( String portText ) {
        int port = Integer.parseInt( portText );
        if( port <= 0 || port >= 65536 )
            throw new IllegalArgumentException( "Port must be between 0 and 65535" );
        return port;
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