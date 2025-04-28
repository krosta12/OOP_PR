package org.example.ooppr.managers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Scale;
import org.example.ooppr.History.DrawAction;

import java.util.ArrayList;
import java.util.List;

public class PaintingZoneManager {
    private Canvas canvas;
    private GraphicsContext gc;

    // -- ZOOMING VARIABLES --
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private final double minZoom = 0.2;
    private final double maxZoom = 3.0;

    // -- DRAWING VARIABLES --
    private char selectedTool = 'b';
    private Color selectedColor = Color.BLACK;
    private double brushSize = 3.0;
    private double lastX;
    private double lastY;

    // -- DRAWING HISTORY --
    private final List<DrawAction> actionsHistory = new ArrayList<>();
    private DrawAction currentAction = null;

    private Color defaultBCColor;


    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    // -- ZOOMING METHODS --
    /**
     * Sets zooming up
     * @param scrollPane - canvas' parent ScrollPane
     */
    public void setupZooming(ScrollPane scrollPane) {
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

        // TODO CTRL + + (Zoom in)
        // TODO CTRL + - (Zoom out)
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
    public void setupDrawing() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function handle and update muse position when it pressed to use "chain of reponsibility"
     * will exec drawPoint funtion with new mouse position parametrs
     */
    private void handleMousePressed(MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();

        // saving current action
        currentAction = new DrawAction(selectedColor, brushSize, selectedTool);

        // draw point on canvas
        drawPoint(lastX, lastY);
    }

    /**
     *
     * @param x point coordinat on plane
     * @param y point coordinat on plane
     * function draw a oval or cycle on plane by x,y params with resourses
     */
    private void drawPoint(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (selectedTool == 'b') {
            gc.setFill(selectedColor);
            gc.fillOval(x - brushSize/2, y - brushSize/2, brushSize, brushSize);
        } else if (selectedTool == 'l') {
            gc.setFill(Color.WHITE); // поменять логику ластика
            gc.fillOval(x - brushSize/2, y - brushSize/2, brushSize, brushSize);
        }
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function ontroll mouse dragged and update mouse position vars to draw line between 2 points
     */
    private void handleMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        // saving current point
        if( currentAction != null ) {
            currentAction.addPoint(currentX, currentY);
        }

        drawLine(lastX, lastY, currentX, currentY);
        lastX = currentX;
        lastY = currentY;
    }

    /**
     *
     * @param startX start coordinate on plane by X axis
     * @param startY start coordinate on plane by Y axis
     * @param endX start coordinate on plane by Y axis
     * @param endY end coordinate on plane by Y axis
     * function draw a line on plane by resourses
     */
    private void drawLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(brushSize);
        gc.setStroke(selectedColor);

        // Smoothing
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        gc.strokeLine(startX, startY, endX, endY);
    }

    /**
     * Set brush size
     * @param brushSize new brush size
     */
    public void setBrushSize(double brushSize) {
        this.brushSize = brushSize;
    }

    /**
     * Set brush color
     * @param color new color
     */
    public void setBrushColor(Color color) {
        this.selectedColor = color;
        gc.setFill(selectedColor);
    }

    private void handleMouseReleased(MouseEvent event) {
        if( currentAction != null ) {
            actionsHistory.add(currentAction);
            currentAction = null;
        }
    }

    // -- ACTIONS HISTORY METHODS --

    /**
     * Undoing last drawing action
     * @param bc default background color
     */
    public void undoLastAction(Color bc) {
        if(!actionsHistory.isEmpty()) {
            actionsHistory.removeLast();
            redrawAll(bc);
        }
    }

    /**
     * Redrawing all the actions
     * @param bc default backgroud color
     */
    private void redrawAll(Color bc) {
        gc.setFill(bc);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for( DrawAction action : actionsHistory ) {
            action.draw(gc);
        }
    }

    /**
     * Setting up CTRL+Z hotkey
     */
    public void setupUndoHotkey() {
        canvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() && event.getCode().toString().equals("Z")) {
                        undoLastAction(defaultBCColor);
                    }
                });
            }
        });
    }

    public void setSelectedTool(char t) {
        selectedTool = t;
    }

    public void setDefaultBCColor(Color color) {
        this.defaultBCColor = color;
    }
}
