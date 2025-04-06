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

    /**
     * The method puts Canvas size and default color
     * @param XResolution canvas width
     * @param YResolution canvas height
     * @param fillColor canvas default background color
     */
    public void initializeCanvas( int XResolution, int YResolution, Color fillColor ) {
        paintingZone.setWidth(XResolution);
        paintingZone.setHeight(YResolution);
        Platform.runLater(() -> fillCanvasColor( fillColor ));
    }

    /**
     * Thw method puts canvas color
     * @param fillColor color that needs to be filled
     */
    private void fillCanvasColor(Color fillColor) {
        GraphicsContext gc = paintingZone.getGraphicsContext2D();
        gc.setFill( fillColor );
        gc.fillRect(0, 0, paintingZone.getWidth(), paintingZone.getHeight());
    }

}
