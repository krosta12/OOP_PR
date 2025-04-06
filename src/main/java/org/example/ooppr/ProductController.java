package org.example.ooppr;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ProductController {

    // Getting elements
    @FXML
    public Canvas paintingZone;

    public void setCanvasSize( int XResolution, int YResolution ) {
        paintingZone.setWidth(XResolution);
        paintingZone.setHeight(YResolution);
        Platform.runLater(() -> fillCanvasColor());
    }

    private void fillCanvasColor() {
        GraphicsContext gc = paintingZone.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, paintingZone.getWidth(), paintingZone.getHeight());
    }

}
