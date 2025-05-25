package org.example.ooppr.ui.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ooppr.ui.controllers.SaveAsDialogController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuBarManager {

    private final MenuBar menuBar;
    private final MenuItem saveAsItem;
    private final MenuItem exitItem;

    private Canvas canvas;

    public MenuBarManager( MenuBar menuBar, MenuItem saveAsItem, MenuItem exitItem ) {
        this.menuBar = menuBar;
        this.saveAsItem = saveAsItem;
        this.exitItem = exitItem;

        setupMenuActions();
    }

    public void setCanvas( Canvas canvas ) {
        this.canvas = canvas;
    }

    private void setupMenuActions() {
        saveAsItem.setOnAction( e -> saveAs() );
        exitItem.setOnAction( e -> exit() );
    }

    private void saveAs() {
        showSaveAsDialog( canvas );
    }

    private void exit() {
        System.out.println( "exit" );
    }

    private void showSaveAsDialog( Canvas canvas ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ooppr/SaveAsDialog.fxml"));
            Parent root = loader.load();

            SaveAsDialogController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Save As");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));

            controller.setDialogStage(dialogStage);

            // Создаем snapshot canvas для превью
            WritableImage snapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, snapshot);
            controller.setPreviewImage(snapshot);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                String path = controller.getFilePath();
                // Сохраняй картинку из snapshot в файл path
                saveImageToFile(snapshot, path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveImageToFile(WritableImage image, String path) {
        System.out.println( "Photo saved to " + path + " (no)" );
    }




}
