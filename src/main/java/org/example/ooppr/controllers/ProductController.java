package org.example.ooppr.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.ooppr.managers.ColorPickerManager;
import org.example.ooppr.managers.PaintingZoneManager;
import org.example.ooppr.managers.ToolsManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    /**
     * Get all var-s from XML
     */
    //get canvas
    @FXML
    public Canvas paintingZone;

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
    private Button TakeBrushButton;
    @FXML
    private Button TakeLastikButton;
    @FXML
    private Slider brushSizeSlider;
    @FXML
    private Text NPixelsText;

    @FXML
    private ScrollPane paintingZoneScrollPane;

    @FXML
    private Label hostJoinLabel;

    @FXML
    private Label ipPortLabel;

    private final PaintingZoneManager paintingZoneManager = new PaintingZoneManager();


    /**
     * initialize an event handlers components for painting zone
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Creating painting zone (canvas) manager, setting functionality up
        paintingZoneManager.setCanvas( paintingZone );
        paintingZoneManager.setupZooming( paintingZoneScrollPane );
        paintingZoneManager.setupDrawing();
        paintingZoneManager.setupUndoHotkey();


        // Attaching buttons functionality
        ColorPickerManager colorPickerManager = new ColorPickerManager( paintingZoneManager );
        colorPickerManager.attachConstantColorToButton(TakeBlackColorConst, Color.BLACK);
        colorPickerManager.attachConstantColorToButton(TakeWhiteColorConst, Color.WHITE);
        colorPickerManager.attachResponsiveColorToButton(TakeLastColorResponsive);
        colorPickerManager.attachColorPicker(TakeCustomColorPanel, TakeLastColorResponsive);

        // Tools functionality
        ToolsManager toolsManager = new ToolsManager( paintingZoneManager );
        toolsManager.attachButton( TakeBrushButton, 'b' );
        toolsManager.attachButton( TakeLastikButton, 'l' );
        toolsManager.attachSlider( brushSizeSlider, NPixelsText );
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
        paintingZoneManager.setDefaultBCColor(fillColor);
        gc.fillRect(0, 0, paintingZone.getWidth(), paintingZone.getHeight());
    }

    public void initializeCanvasByHistory() {

    }

    public void setIpPort(String ipPort) {
        hostJoinLabel.setText( "Server started at: ");
        ipPortLabel.setText( ipPort );
    }
}