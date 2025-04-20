package org.example.ooppr.managers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class PaintingZoneManager {
    private final Canvas canvas;
    private final GraphicsContext gc;

    // -- ZOOMING VARIABLES --
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private final double minZoom = 0.2;
    private final double maxZoom = 3.0;

    // -- DRAWING VARIABLES --
    private char selectedTool = 'b';
    private Color selectedColor = Color.BLACK;
    private double brushSize = 3;

    public PaintingZoneManager(Canvas canvas, ScrollPane scrollPane) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        setupZooming(scrollPane);
        setupDrawing();
    }

    // -- ZOOMING METHODS --
    /**
     * Sets zooming up
     * @param scrollPane - canvas' parent ScrollPane
     */
    private void setupZooming(ScrollPane scrollPane) {
        scrollPane.setOnScroll( (ScrollEvent event) -> {

            if( event.isControlDown() ) {
                double dy = event.getDeltaY();
                if(dy == 0) return;

                if( dy > 0 ) zoomFactor = Math.min(zoomFactor + zoomStep, maxZoom);
                else zoomFactor = Math.max(zoomFactor - zoomStep, minZoom);

                applyZoom();
                event.consume();
            }

        });
    }

    /**
     * Method applies zoom changes
     */
    private void applyZoom() {
        canvas.getTransforms().clear();
        canvas.getTransforms().add( new Scale( zoomFactor, zoomFactor, 0, 0 ) );
    }

    /**
     * Resets zoom
     */
    public void resetZoom() {
        zoomFactor = 1.0;
        applyZoom();
    }

    // -- DRAWING METHODS --

    /**
     * Sets drawing up
     */
    private void setupDrawing() {
    }
}
