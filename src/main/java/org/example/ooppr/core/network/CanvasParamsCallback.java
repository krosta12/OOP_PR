package org.example.ooppr.core.network;
import javafx.scene.paint.Color;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.ui.managers.PaintingZoneManager;

import java.util.List;

public interface CanvasParamsCallback {
    void onParamsReceived(int xResolution, int yResolution, Color defaultColor);
    void onParamsReceived(List<DrawAction> actionsHistory);
    void onDrawActionRecieved(PaintingZoneManager paintingZoneManager);
    void onError(String message);
}