package org.example.ooppr.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SaveAsDialogController {
    @FXML
    private ImageView previewImageView;
    @FXML private TextField pathTextField;

    private Stage dialogStage;
    private boolean saveClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setPreviewImage(Image image) {
        previewImageView.setImage(image);
    }

    public String getFilePath() {
        return pathTextField.getText();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void onSave() {
        if (getFilePath() == null || getFilePath().isEmpty()) {
            return;
        }
        saveClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
