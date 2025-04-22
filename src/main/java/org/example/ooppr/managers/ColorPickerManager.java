package org.example.ooppr.managers;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorPickerManager {

    private final PaintingZoneManager paintingZoneManager;
    private Color lastColor;

    public ColorPickerManager(PaintingZoneManager paintingZoneManager) {
        this.paintingZoneManager = paintingZoneManager;
        this.lastColor = Color.BLACK;
    }

    /**
     * Attaching color to button
     * @param button attached button
     * @param color attached color
     */
    public void attachConstantColorToButton(Button button, Color color) {
        button.setOnAction(event -> paintingZoneManager.setBrushColor(color));
    }

    /**
     * Attaching color to button
     * @param button attached button
     */
    public void attachResponsiveColorToButton(Button button) {
        button.setOnAction(event -> {
            paintingZoneManager.setBrushColor(lastColor);
            updateLastColorButton(button, lastColor);
        });
    }

    /**
     * Attaches Color Picker
     * @param colorPicker color picker reference
     * @param lastColorResponsiveButton last color button
     */
    public void attachColorPicker(ColorPicker colorPicker, Button lastColorResponsiveButton) {
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            paintingZoneManager.setBrushColor(selectedColor);
            lastColor = selectedColor;
            updateLastColorButton(lastColorResponsiveButton, selectedColor);
        });
    }

    /**
     * Updates buttons backgroud color
     */
    private void updateLastColorButton(Button button, Color color) {
        double red = color.getRed() * 255;
        double green = color.getGreen() * 255;
        double blue = color.getBlue() * 255;

        button.setStyle("-fx-background-color: rgb(" + red + "," + green + "," + blue + ")" );
    }

}
