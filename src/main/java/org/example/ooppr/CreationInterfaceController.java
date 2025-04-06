package org.example.ooppr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreationInterfaceController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    public TextField XResolutionHolder;

    @FXML
    public TextField YResolutionHolder;

    /**
     * The method switches the scene to Product. Checks the sizes of the passed canvas XResolutionHolder and YResolutionHolder.
     */
    public void SwitchToPaintPanel(ActionEvent event) throws IOException {

        try {
            // Getting canvas resolution
            int XResolution = Integer.parseInt(XResolutionHolder.getText());
            int YResolution = Integer.parseInt(YResolutionHolder.getText());

            if ( resolutionIsValid(XResolution, YResolution) ) {
                // Loading scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
                root = loader.load();
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

                // getting controller, canvas sizing
                ProductController productController = loader.getController();
                productController.setCanvasSize(XResolution, YResolution);

                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                // if negative value or 0 then alerts error
                showAlert("Resolution error", "Wrong resolution!", "Please enter a valid resolution (positive integer).");
            }
        } catch (NumberFormatException e) {
            // If not-numerical value then alerts error
            showAlert("Input error", "Invalid input!", "Please input numerical values.");
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
        alert.setTitle( title );
        alert.setHeaderText( header );
        alert.setContentText( content );
        alert.showAndWait();
    }

}
