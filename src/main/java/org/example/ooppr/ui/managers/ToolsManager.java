package org.example.ooppr.ui.managers;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class ToolsManager {

    private PaintingZoneManager paintingZoneManager;

    public ToolsManager(PaintingZoneManager paintingZoneManager) {
        this.paintingZoneManager = paintingZoneManager;
    }

    /**
     * Attaches button functionality
     * @param button button
     * @param t mode ('b' - brush, 'l' - lastik)
     */
    public void attachButton( Button button, char t ) {
        button.setOnMouseClicked( e -> {
            paintingZoneManager.setSelectedTool( t );
        });
    }

    /**
     * Sets brush size
     * @param slider slider
     */
    public void attachSlider( Slider slider, Text pixelsText ) {
        slider.setOnMouseReleased( e -> {
            double value = slider.getValue();
           paintingZoneManager.setBrushSize( value);
           pixelsText.setText( "" + (int) value );
        });
    }

}
