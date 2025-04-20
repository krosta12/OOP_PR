package org.example.ooppr.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.example.ooppr.managers.ColorPickerManager;
import org.example.ooppr.managers.PaintingZoneManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
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

    @FXML
    private ScrollPane paintingZoneScrollPane;

    @FXML
    private Pane canvasContainer;

    //init standart rom nullpointerExcetption
    private char selectedTool = 'b'; //придумаешь лучше - поменяй switch-case
    private Color selectedColor = Color.BLACK;

    private double brushSize = 5.0; //slider Text привязать надо
    private double lastX, lastY;

    /**
     * initialize an event handlers components for painting zone
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Creating painting zone (canvas) manager, setting functionality up
        PaintingZoneManager paintingZoneManager = new PaintingZoneManager(paintingZone, paintingZoneScrollPane);
        paintingZoneManager.setupZooming( paintingZoneScrollPane );
        paintingZoneManager.setupDrawing();

        // Attaching buttons functionality
        ColorPickerManager colorPickerManager = new ColorPickerManager( paintingZoneManager );
        colorPickerManager.attachConstantColorToButton(TakeBlackColorConst, Color.BLACK);
        colorPickerManager.attachConstantColorToButton(TakeWhiteColorConst, Color.WHITE);
        colorPickerManager.attachResponsiveColorToButton(TakeLastColorResponsive);
        colorPickerManager.attachColorPicker(TakeCustomColorPanel, TakeLastColorResponsive);

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