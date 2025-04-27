package org.example.ooppr.Server;
import javafx.scene.paint.Color;
import org.example.ooppr.History.DrawAction;

import java.util.List;

public interface CanvasParamsCallback {
    void onParamsReceived(int xResolution, int yResolution, Color defaultColor);
    void onParamsReceived(List<DrawAction> actionsHistory);
    void onError(String message);
}