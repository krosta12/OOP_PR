package org.example.ooppr.Server;
import javafx.scene.paint.Color;

public interface CanvasParamsCallback {
    void onParamsReceived(int xResolution, int yResolution, Color defaultColor);
    void onError(String message);
}