package org.example.ooppr.core.drawing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

import java.util.ArrayList;
import java.util.List;

public class DrawAction {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private final double brushSize;
    private final char toolType; // 'b' - brush, 'l' - eraser

    public DrawAction(Color color, double brushSize, char toolType) {
        this.color = color;
        this.brushSize = brushSize;
        this.toolType = toolType;
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public void draw(GraphicsContext gc) {
        if( points.isEmpty() ) return;

        gc.setLineWidth(brushSize);
        gc.setStroke(color);
        gc.setFill(color);

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
