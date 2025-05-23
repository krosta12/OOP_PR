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
import org.example.ooppr.core.network.Client;
import org.example.ooppr.core.network.Server;
import org.example.ooppr.core.network.protocol.NotUniqueNicknameException;
import org.example.ooppr.core.users.User;
import org.example.ooppr.ui.managers.ColorPickerManager;
import org.example.ooppr.ui.managers.ConnectionsManager;
import org.example.ooppr.ui.managers.PaintingZoneManager;
import org.example.ooppr.ui.managers.ToolsManager;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    /**
     * Get all var-s from XML
     */
    //get canvas
    @FXML
    public Canvas paintingZone;
    public VBox connectionsWrapper;

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

    private Stage stage;
    private Client client;
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
        paintingZoneManager.setupUndoHotkey();

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

        // Connections info

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

    public void connectToHost(String ip, int port, User user ) {
        connectionsManager = new ConnectionsManager( connectionsWrapper );
        this.client = new Client( paintingZoneManager, connectionsManager );
        this.user = user;

        client.connect(ip, port, user);
        paintingZoneManager.setClient( client, user );
        connectionsManager.setClient( client, user );
        client.setClientEventListener(new ClientEventListener() {
            @Override
            public void onKick() {
                Platform.runLater( () -> {
                    showKickAlert();
                    closeProgram();
                } );
            }

            @Override
            public void onNicknameNotUnique() {
                Platform.runLater( () -> {
                    showNotUniqueNicknameAlert();
                    closeProgram();
                } );

            }

            @Override
            public void onDisconnect() {

            }
        });
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
     * Shows user exception notification
     */
    private void showNotUniqueNicknameAlert() {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/ooppr/shocked-smile.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(120);

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "Nickname exception" );
        alert.setHeaderText( "This nickname is already taken!" );
        alert.setContentText( "Please choose another one." );
        alert.showAndWait();
    }


}