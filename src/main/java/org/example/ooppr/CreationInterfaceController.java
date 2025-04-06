package org.example.ooppr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void SwitchToPaintPanel(ActionEvent event) throws IOException {
        // Loading scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        // Getting canvas resolution and setting it
        int XResolution = Integer.parseInt(XResolutionHolder.getText());
        int YResolution = Integer.parseInt(YResolutionHolder.getText());

        ProductController productController = loader.getController();
        productController.setCanvasSize(XResolution, YResolution);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
