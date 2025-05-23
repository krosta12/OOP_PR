package org.example.ooppr.ui.managers;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Scale;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.Client;
import org.example.ooppr.core.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaintingZoneManager {

    //sendThreshold модет просадить сервер пожтому нужно найти оптимальное значени или изменять его поп мере прибавления кливентов по формуле квадратов чтоб не повесить сервер
    private final double sendThreshold = 20;      // Переменная мат. шага по длинне вектора относительно начальной точки - меньше значнеие - большая плотность обновления но и большее колличество пакетов
    private double lastSentX, lastSentY; // точка начала вектора



    private Canvas canvas;
    private GraphicsContext gc;

    // -- ZOOMING VARIABLES --
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private final double minZoom = 0.2;
    private final double maxZoom = 3.0;

    // -- DRAWING VARIABLES --
    private char selectedTool = 'b';
    private Color selectedColor = Color.BLACK;
    private double brushSize = 3.0;
    private double lastX;
    private double lastY;

    // -- DRAWING HISTORY --
    private final List<DrawAction> actionsHistory = new ArrayList<>();

    private DrawAction fullAction;      // накопит все точки для истории чтоб передача была частичной но откат полным
    private DrawAction fragmentAction;  // будет сбрасываться после каждой отправки


    private Color defaultBCColor;
    private Client client;
    private String nickname;
    private User user;


    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void initializeCanvas(int XResolution, int YResolution, String fillColorWeb) {
        canvas.setWidth(XResolution);
        canvas.setHeight(YResolution);

        Platform.runLater(() -> fillCanvasColor( Color.web(fillColorWeb) ));
    }

    private void fillCanvasColor(Color fillColor) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(fillColor);
        setDefaultBCColor(fillColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // -- ZOOMING METHODS --
    /**
     * Sets zooming up
     * @param scrollPane - canvas' parent ScrollPane
     */
    public void setupZooming(ScrollPane scrollPane) {
        scrollPane.setOnScroll( (ScrollEvent event) -> {

            if( event.isControlDown() ) {
                double dy = event.getDeltaY();
                if(dy == 0) return;

                if( dy > 0 ) zoomFactor = Math.min(zoomFactor + zoomStep, maxZoom);
                else zoomFactor = Math.max(zoomFactor - zoomStep, minZoom);

                applyZoom();
                event.consume();
            }

        });

        // TODO CTRL + + (Zoom in)
        // TODO CTRL + - (Zoom out)
    }

    /**
     * Method applies zoom changes
     */
    private void applyZoom() {
        canvas.getTransforms().clear();
        canvas.getTransforms().add( new Scale( zoomFactor, zoomFactor, 0, 0 ) );
    }

    /**
     * Resets zoom
     */
    public void resetZoom() {
        zoomFactor = 1.0;
        applyZoom();
    }

    // -- DRAWING METHODS --

    /**
     * Sets drawing up
     */
    public void setupDrawing() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased); //get as warn
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function handle and update muse position when it pressed to use "chain of reponsibility"
     * will exec drawPoint funtion with new mouse position parametrs
     */
    private void handleMousePressed(MouseEvent event) {
        lastX = event.getX();
        lastY = event.getY();

        lastSentX = lastX;// задаём точку начала от которой считаем векторное растояние
        lastSentY = lastY;

        // saving current action in 2 var-s for backtracking after packt distibution
        fullAction = new DrawAction(selectedColor.toString(), brushSize, selectedTool);
        fragmentAction = new DrawAction(selectedColor.toString(), brushSize, selectedTool);


        fullAction.addPoint(lastX, lastY);
        fragmentAction.addPoint(lastX, lastY);

        // draw point on canvas
        drawPoint(lastX, lastY);
    }

    /**
     *
     * @param x point coordinat on plane
     * @param y point coordinat on plane
     * function draw a oval or cycle on plane by x,y params with resourses
     */
    public void drawPoint(double x, double y) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (selectedTool == 'b') {
            gc.setFill(selectedColor);
            gc.fillOval(x - brushSize/2, y - brushSize/2, brushSize, brushSize);
        } else if (selectedTool == 'l') {
            gc.setFill(Color.WHITE); // поменять логику ластика
            gc.fillOval(x - brushSize/2, y - brushSize/2, brushSize, brushSize);
        }
    }

    /**
     *
     * @param event SceneBuilder event handler - contains info about target object
     * function ontroll mouse dragged and update mouse position vars to draw line between 2 points
     */
    private void handleMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        // saving current point as parts
        if (fullAction != null) {
            fullAction.addPoint(currentX, currentY);
            fragmentAction.addPoint(currentX, currentY);
        }

        drawLine(lastX, lastY, currentX, currentY);

        double dx = currentX - lastSentX; //дельта на каждую ось для подсчёта длинны
        double dy = currentY - lastSentY;

        if (Math.hypot(dx, dy) >= sendThreshold) { // мат. вект. длинна сравниваем с шагом на каждом фрейме обновления позиции мышки
            client.sendDrawAction(fragmentAction, user); // елсли изменения существенны
            fragmentAction = new DrawAction(selectedColor.toString(), brushSize, selectedTool);
            fragmentAction.addPoint(currentX, currentY);
            lastSentX = currentX; //обновляем позиции точки для нового построяния вектора
            lastSentY = currentY;
        }

        lastX = currentX;
        lastY = currentY;
    }

    /**
     *
     * @param startX start coordinate on plane by X axis
     * @param startY start coordinate on plane by Y axis
     * @param endX start coordinate on plane by Y axis
     * @param endY end coordinate on plane by Y axis
     * function draw a line on plane by resourses
     */
    private void drawLine(double startX, double startY, double endX, double endY) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(brushSize);
        gc.setStroke(selectedColor);

        // Smoothing
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        gc.strokeLine(startX, startY, endX, endY);
    }

    /**
     * Set brush size
     * @param brushSize new brush size
     */
    public void setBrushSize(double brushSize) {
        this.brushSize = brushSize;
    }

    /**
     * Set brush color
     * @param color new color
     */
    public void setBrushColor(Color color) {
        this.selectedColor = color;
        gc.setFill(selectedColor);
    }

    private void handleMouseReleased(MouseEvent event) {
        if( fullAction != null ) {

            if( client != null ) {
                client.sendDrawAction(fragmentAction, user); //мы отправим кусок рисунка а не весь чтоб не накладывать их друг на друга
            }

            actionsHistory.add(fullAction); // добавим ЦЕЛУЮ фигуру чтоб откат был не по частям
            fullAction = null; // закончить процесс рисования
            fragmentAction = null;
        }
    }

    // -- ACTIONS HISTORY METHODS --

    /**
     * Undoing last drawing action
     */
    public void undoLastAction( String undoerNickname ) {
        if(!actionsHistory.isEmpty()) {
            System.out.println( "UNDOING" );
            actionsHistory.removeLast();
            redrawAll( defaultBCColor );
        }
    }

    /**
     * Redrawing all the actions
     * @param bc default backgroud color
     */
    private void redrawAll(Color bc) {
        gc.setFill(bc);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for( DrawAction action : actionsHistory ) {
            action.draw(gc);
        }
    }

    /**
     * Setting up CTRL+Z hotkey
     */
    public void setupUndoHotkey() {
        canvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.isControlDown() && event.getCode().toString().equals("Z")) {
                        undoLastAction( nickname );
                        client.undo( nickname );
                        System.out.println( "CTRL+Z" );
                    }
                });
            }
        });
    }

    public void setSelectedTool(char t) {
        selectedTool = t;
    }

    public void setDefaultBCColor(Color color) {
        this.defaultBCColor = color;
    }

    public void drawByDrawAction(DrawAction action, User sender) {

        if(!Objects.equals(user.getNickname(), sender.getNickname())) {
            actionsHistory.add( action );
        }
        Platform.runLater( () -> {
            Color lastColor = selectedColor;
            action.draw(gc);
            gc.setFill(lastColor);
        } );
    }

    public void setClient( Client client, User user ) {
        this.client = client;
        this.user = user;
    }
}
