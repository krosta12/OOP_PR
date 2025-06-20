package org.example.ooppr.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.ooppr.core.ClientEventListener;
import org.example.ooppr.core.DrawingEventListener;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.Client;
import org.example.ooppr.core.network.Server;
import org.example.ooppr.core.users.User;
import org.example.ooppr.ui.managers.*;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductController implements Initializable, ClientEventListener, DrawingEventListener {
    /**
     * Get all var-s from XML
     */
    //get canvas
    @FXML
    public Canvas paintingZone;
    public VBox connectionsWrapper;
    public MenuItem MenuItemSaveAs;
    public MenuItem MenuItemExit;
    public MenuBar menuBar;

    //get color system buttons
    @FXML
    private Button TakeBlackColorConst;
    @FXML
    private Button TakeWhiteColorConst;
    @FXML
    private Button TakeLastColorResponsive;
    @FXML
    private ColorPicker TakeCustomColorPanel;

    @FXML
    private Button TakeBrushButton;
    @FXML
    private Button TakeLastikButton;
    @FXML
    private Slider brushSizeSlider;
    @FXML
    private Text NPixelsText;

    @FXML
    private ScrollPane paintingZoneScrollPane;

    @FXML
    private Label hostJoinLabel;

    @FXML
    private Label ipPortLabel;

    private final PaintingZoneManager paintingZoneManager = new PaintingZoneManager();
    private ConnectionsManager connectionsManager;
    private MenuBarManager menuBarManager;

    private Stage stage;
    private Client client;
    private Server server;
    private User user;


    /**
     * initialize an event handlers components for painting zone
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Creating painting zone (canvas) manager, setting functionality up
        paintingZoneManager.setCanvas( paintingZone );
        paintingZoneManager.setupZooming( paintingZoneScrollPane );
        paintingZoneManager.setupDrawing();
//        paintingZoneManager.setupUndoHotkey();

        // Attaching buttons functionality
        ColorPickerManager colorPickerManager = new ColorPickerManager( paintingZoneManager );
        colorPickerManager.attachConstantColorToButton(TakeBlackColorConst, Color.BLACK);
        colorPickerManager.attachConstantColorToButton(TakeWhiteColorConst, Color.WHITE);
        colorPickerManager.attachResponsiveColorToButton(TakeLastColorResponsive);
        colorPickerManager.attachColorPicker(TakeCustomColorPanel, TakeLastColorResponsive);

        // Tools functionality
        ToolsManager toolsManager = new ToolsManager( paintingZoneManager );
        toolsManager.attachButton( TakeBrushButton, 'b' );
        toolsManager.attachButton( TakeLastikButton, 'l' );
        toolsManager.attachSlider( brushSizeSlider, NPixelsText );

        menuBarManager = new MenuBarManager( menuBar, MenuItemSaveAs, MenuItemExit );
        menuBarManager.setCanvas( paintingZone );
    }


    /**
     * The method puts Canvas size and default color
     * @param XResolution canvas width
     * @param YResolution canvas height
     * @param fillColor canvas default background color
     */
    public void initializeCanvas(int XResolution, int YResolution, Color fillColor) {
        paintingZoneManager.initializeCanvas(XResolution, YResolution, fillColor.toString());
    }

    public void setIpPort(String ip, int port) {
        hostJoinLabel.setText( "Connected to server: ");
        ipPortLabel.setText( ip + ":" + port );
    }

    public void startAsServer(String ip, int port, User creatorUser, Server server) {
        connectToHost( ip, port, creatorUser );
        this.server = server;
    }

    public void connectToHost(String ip, int port, User user ) {
        connectionsManager = new ConnectionsManager( connectionsWrapper );
        this.client = new Client();
        this.user = user;

        client.connect(ip, port, user);
        paintingZoneManager.setClient( client, user );
        connectionsManager.setClient( client, user );
        client.setClientEventListener( this );
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest( e -> {
            boolean exit = askUserAlert();
            if( exit )
                closeProgram();
            else
                e.consume();
        } );
    }

    /**
     * Closing socket and exiting program
     */
    private void closeProgram() {

        if( client != null )
            client.disconnect( user );

        if ( server != null )
            server.stop();

        Platform.exit();
    }

    /**
     * Asking user for exiting confirmation
     * @return boolean does user want to exit
     */
    private boolean askUserAlert() {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setTitle( "Confirm exit" );
        alert.setHeaderText(null);
        alert.setContentText( "Are you sure you wanna exit?" );
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        return result == ButtonType.OK;
    }


    /**
     * Shows user notification that he was kicked (he-he-he)
     */
    private void showKickAlert() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/ooppr/sad-smile.jpg")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(120);

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Kicked" );
        alert.setHeaderText( "You were kicked from server" );
        alert.setContentText( "The application will close now" );
        alert.getDialogPane().setGraphic(imageView);
        alert.showAndWait();
    }

    /**
     * Shows user notification that he was banned (he-he-he)
     */
    private void showBanAlert() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/ooppr/angry-smile.jpg")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(120);

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Banned" );
        alert.setHeaderText( "You were banned!" );
        alert.setContentText( "The application will close now" );
        alert.getDialogPane().setGraphic(imageView);
        alert.showAndWait();
    }

    /**
     * Shows user exception notification
     */
    private void showExceptionMessage( Exception e ) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/ooppr/shocked-smile.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(120);

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "User exception" );
        alert.setHeaderText( null );
        alert.setContentText( e.getMessage() );
        alert.getDialogPane().setGraphic(imageView);
        alert.showAndWait();
    }

    // -- CLIENT EVENT LISTENER --
    // <editor-fold desc="Client events listener">


    @Override
    public void onKick() {
        Platform.runLater( () -> {
            showKickAlert();
            closeProgram();
        } );
    }

    @Override
    public void onBan() {
        Platform.runLater( () -> {
            showBanAlert();
            closeProgram();
        } );
    }

    @Override
    public void onInitializeCanvas( int xResolution, int yResolution, String colorWeb, List<DrawAction> actions ) {
        initializeCanvas( xResolution, yResolution, Color.valueOf( colorWeb ) );
        // drawing all by history
        for( DrawAction action : actions ) {
            paintingZoneManager.drawByDrawAction(action, user);
        }
    }

    @Override
    public void onDrawActionMessageReceived(DrawAction action, User sender) {
        paintingZoneManager.drawByDrawAction( action, sender );
    }

    @Override
    public void onUndo() {
        System.out.println( "[CLIENT] UNDO REQUIRED" );
    }

    @Override
    public void onNewUsersList(List<User> usersList) {
        Platform.runLater( () -> connectionsManager.setList( usersList ));
    }

    @Override
    public void onException( Exception e ) {
        Platform.runLater( () -> {
            showExceptionMessage( e );
            closeProgram();
        } );
    }

    @Override
    public void onUserRoleChanged(List<User> newUsersList) {
        for( User u : newUsersList ) {
            if( u.getNickname().equals( user.getNickname() ) ) {
                user.setRole( u.getRole() );
                System.out.println( user.getRole() );
            }
        }
        Platform.runLater( () -> connectionsManager.setList( newUsersList ));

    }

    @Override
    public void onDisconnect() {

    }

    // </editor-fold>

    // -- DRAWING EVENT LISTENER --
    // <editor-fold desc="Drawing events listener">



    // </editor-fold>

}