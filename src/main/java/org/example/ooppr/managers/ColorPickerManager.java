package org.example.ooppr.managers;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorPickerManager {

    private final PaintingZoneManager paintingZoneManager;

    public ColorPickerManager(PaintingZoneManager paintingZoneManager) {
        this.paintingZoneManager = paintingZoneManager;
    }

    /**
     * Attaching color to button
     * @param button attached button
     * @param color attached color
     */
    public void attachColorToButton(Button button, Color color) {
        button.setOnAction(event -> paintingZoneManager.setBrushColor(color));
    }

    public void attachColorPicker(ColorPicker colorPicker) {
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            paintingZoneManager.setBrushColor(selectedColor);
        });
    }

}
