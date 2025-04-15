package org.example.ooppr;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ProductController {
    /**
     * Get all var-s from XML
     */
    //get canvas
    @FXML
    public Canvas paintingZone;

    //get brush buttons (need refactor)
    @FXML
    private Button takeBrushButton;
    @FXML
    private Button takeLastikButton; //WARNING may to create a dict of items

    //get color system buttons
    @FXML
    private Button TakeBlackColorConst;
    @FXML
    private Button TakeWhiteColorConst;
    @FXML
    private Button TakeLastColorResponsive;
    @FXML
    private ColorPicker TakeCustomColorPanel;

    //init standart rom nullpointerExcetption
    private char selectedTool = 'b'; //придумаешь лучше - поменяй switch-case
    private Color selectedColor = Color.BLACK;
    private Color lastColor = Color.BLACK;
    private double prevX, prevY;

    private double brushSize = 5.0; //slider Text привязать надо

    /**
     * initialize a event handlers components for painting zone
     */
    public void initialize() {
        paintingZone.setOnMousePressed(this::handleMousePressed);
        paintingZone.setOnMouseDragged(this::handleMouseDragged);
        paintingZone.setOnMouseReleased(this::handleMouseReleased);
    }

    /**
     * The method puts Canvas size and default color
     * @param XResolution canvas width
     * @param YResolution canvas height
     * @param fillColor canvas default background color
     */
    public void initializeCanvas(int XResolution, int YResolution, Color fillColor) {
        paintingZone.setWidth(XResolution);
        paintingZone.setHeight(YResolution);
        Platform.runLater(() -> fillCanvasColor(fillColor));

        this.selectedColor = fillColor;
        this.lastColor = fillColor;
    }

    /**
     * Thw method puts canvas color
     * @param fillColor color that needs to be filled
     */
    private void fillCanvasColor(Color fillColor) {
        GraphicsContext gc = paintingZone.getGraphicsContext2D();
        gc.setFill(fillColor);
        gc.fillRect(0, 0, paintingZone.getWidth(), paintingZone.getHeight());
    }

    /**
     *
     * @param event param from SceneBuilder where contains info about target object
     * function using switch-case logic (now if-else) change a painting tool to smth from dictionary
     */
    @FXML
    public void setSelectedTool(ActionEvent event) {
        Object source = event.getSource(); //get info about target object
        if (source == takeBrushButton) { //compare to object
            selectedTool = 'b';
        } else if (source == takeLastikButton) {
            selectedTool = 'l';
        }
    }


    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function use if-else logic to change color and save last custom picked color from color picker
     */
    @FXML
    public void pickColor(ActionEvent event) {
        //WARNING must to recreate it by switch-case construction
        Object source = event.getSource(); //parse info

        if (source != TakeLastColorResponsive) {
            lastColor = selectedColor; //if new color was picked - replace to temp var for future
        }

        if (source == TakeBlackColorConst) { //WARNING need to use DICT
            selectedColor = Color.BLACK;
        } else if (source == TakeWhiteColorConst) {
            selectedColor = Color.WHITE;
        } else if (source == TakeLastColorResponsive) {
            selectedColor = lastColor;
        } else if (source == TakeCustomColorPanel) {
            selectedColor = TakeCustomColorPanel.getValue(); //get colorData
        }
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function handle and update muse position when it pressed to use "chain of reponsibility"
     * will exec drawPoint funtion with new mouse position parametrs
     */
    private void handleMousePressed(MouseEvent event) {
        prevX = event.getX();
        prevY = event.getY();

        drawPoint(event.getX(), event.getY());
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function ontroll mouse dragged and update mouse position vars to draw line between 2 points
     */
    private void handleMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        drawLine(prevX, prevY, currentX, currentY); //добавить smooth

        prevX = currentX;
        prevY = currentY;
    }

    private void handleMouseReleased(MouseEvent event) {
        // Можно добавить логику при отпускании кнопки мыши но я не знаю зачем оно нам надо
    }

    /**
     *
     * @param x point coordinat on plane
     * @param y point coordinat on plane
     * function draw a oval or cycle on plane by x,y params with resourses
     */
    private void drawPoint(double x, double y) {
        GraphicsContext gc = paintingZone.getGraphicsContext2D();

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
     * @param startX start coordinate on plane by X axis
     * @param startY start coordinate on plane by Y axis
     * @param endX start coordinate on plane by Y axis
     * @param endY end coordinate on plane by Y axis
     * function draw a line on plane by resourses
     */
    private void drawLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = paintingZone.getGraphicsContext2D();
        //WARNING may change draw logic
        if (selectedTool == 'b') {
            gc.setStroke(selectedColor);
            gc.setLineWidth(brushSize);
            gc.strokeLine(startX, startY, endX, endY);
        } else if (selectedTool == 'l') {
            gc.setStroke(Color.WHITE); //WARNING поменять на fillColor
            gc.setLineWidth(brushSize);
            gc.strokeLine(startX, startY, endX, endY);
        }
    }

    /**
     *
     * @param size parameter for setter
     * function change brush size var (created for encapsulation)
     */
    public void setBrushSize(double size) {
        this.brushSize = size;
    }

    /**
     *
     * @param event MouseEvent event from SceneBuilder lib
     * function change brush size by private brushSize var
     */
    @FXML
    private void handleBrushSizeChange(MouseEvent event) {
        Slider slider = (Slider) event.getSource();
        setBrushSize(slider.getValue());
    }
}