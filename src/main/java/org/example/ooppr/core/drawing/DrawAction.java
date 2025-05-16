package org.example.ooppr.core.drawing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DrawAction implements Serializable {
    private final List<Point> points = new ArrayList<>();
    private final String colorWeb;
    private final double brushSize;
    private final char toolType; // 'b' - brush, 'l' - eraser

    public DrawAction(String colorWeb, double brushSize, char toolType) {
        this.colorWeb = colorWeb;
        this.brushSize = brushSize;
        this.toolType = toolType;
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public void draw(GraphicsContext gc) {
        if( points.isEmpty() ) return;

        gc.setLineWidth(brushSize);
        gc.setStroke( Color.web( colorWeb ) );
        gc.setFill( Color.web( colorWeb ) );

        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        Point prev = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point curr = points.get(i);
            gc.strokeLine(prev.x, prev.y, curr.x, curr.y);
            prev = curr;
        }
    }
}
