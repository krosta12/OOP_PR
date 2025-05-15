package org.example.ooppr.core.network.protocol;

import javafx.scene.paint.Color;
import org.example.ooppr.core.drawing.DrawAction;

import java.util.List;

public class CanvasStateMessage extends Message{
    private static final long serialVersionUID = 1L;

    private final List<DrawAction> actions;
    private final int xResolution;
    private final int yResolution;
    private final String colorWeb;


    public CanvasStateMessage(List<DrawAction> drawActions, int xResolution, int yResolution, String colorWeb) {
        super(MessageType.CANVAS_STATE);
        this.actions = drawActions;
        this.xResolution = xResolution;
        this.yResolution = yResolution;
        this.colorWeb = colorWeb;
    }

    // -- GETTERS --
    public List<DrawAction> getDrawActions() {
        return actions;
    }

    public int getxResolution() {
        return xResolution;
    }

    public int getyResolution() {
        return yResolution;
    }

    public String getColorWeb() {
        return colorWeb;
    }

}
