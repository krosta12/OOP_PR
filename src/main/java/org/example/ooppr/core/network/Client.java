package org.example.ooppr.core.network;

import javafx.application.Platform;
import org.example.ooppr.Server.Data;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.*;
import org.example.ooppr.core.users.User;
import org.example.ooppr.ui.managers.ConnectionsManager;
import org.example.ooppr.ui.managers.PaintingZoneManager;

import java.io.*;
import java.net.*;
import java.util.List;

public class Client {


    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Thread clientThread;

    private final PaintingZoneManager paintingZoneManager;
    private final ConnectionsManager connectionsManager;

    public Client( PaintingZoneManager paintingZoneManager, ConnectionsManager connectionsManager ) {
        this.paintingZoneManager = paintingZoneManager;
        this.connectionsManager = connectionsManager;
    }

    //WARN DOC
    public void connect(String ip, int port, User user ) {
        clientThread = new Thread(() -> { //WARN RECHECK ARROW FUNC //WARN RECHECK MEMORY AFTER NEW OBJECT
            try {
                socket = new Socket(ip, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // First OUT message
                InitUserMessage initUserMsg = new InitUserMessage( user );
                out.writeObject( initUserMsg );
                out.flush();

                // Get connected users list
                Object msg = in.readObject();
                if( msg instanceof UserConnectedMessage userConnectedMessage ) {
                    Platform.runLater( () -> connectionsManager.setList( userConnectedMessage.getNewUsersList() ));
                }

                // Canvas initialization
                msg = in.readObject();

                if( msg instanceof CanvasStateMessage canvasMsg) {
                    paintingZoneManager.initializeCanvas(
                            canvasMsg.getxResolution(),
                            canvasMsg.getyResolution(),
                            canvasMsg.getColorWeb()
                    );

                    // drawing all by history
                    for( DrawAction action : canvasMsg.getDrawActions() ) {
                        paintingZoneManager.drawByDrawAction(action, "init");
                    }
                }

                while (true) { // getting all new messages
                    System.out.println( "[CLIENT] received new drawing message" );

                    Object data = in.readObject();

                    if( data instanceof DrawActionMessage drawMsg ) {
                        DrawAction action = drawMsg.getDrawAction();
                        paintingZoneManager.drawByDrawAction( action, drawMsg.getNickname() );

                    } else if ( data instanceof UndoMessage undoMsg ) {
                        paintingZoneManager.undoLastAction( undoMsg.getNickname() );
                    } else if ( data instanceof UserConnectedMessage userConnectedMessage) {
                        // show toast message?
                        Platform.runLater( () -> connectionsManager.setList( userConnectedMessage.getNewUsersList() ));
                    } else if ( data instanceof UserDisconnectedMessage userDisconnectedMessage ) {
                        // show toast message?
                        System.out.println( "[CLIENT] disconnected" );
                        Platform.runLater( () -> connectionsManager.setList( userDisconnectedMessage.getNewUsersList() ));

                    }
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] Error: " + e.getMessage());
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }

    //WARN DOC
    public void sendDrawAction(DrawAction action, String nickname) {
        try {
            DrawActionMessage msg = new DrawActionMessage(action, nickname);
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) { //WARN TRANSLATE
            System.out.println("[CLIENT] ! Failed to send message");
            System.out.println( e.getMessage() );
        }
    }

    private void sendDisconnectMessage( User user ) {
        try {
            DisconnectMessage disconnectMessage = new DisconnectMessage( user );
            out.writeObject(disconnectMessage);
            out.flush();
        } catch (IOException e) {
            System.out.println( "[CLIENT] ! Failed to send disconnection message: " + e.getMessage() );
        }
    }

    public void undo( String nickname ) {
        try {
            UndoMessage msg = new UndoMessage( nickname );
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            System.out.println( "[CLIENT] UNDO exception: " + e.getMessage());
        }
    }

    public void disconnect( User user )  {
        try {
            if( socket != null && !socket.isClosed() ) {
                sendDisconnectMessage( user );
                socket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if( clientThread != null && clientThread.isAlive() ) {
            clientThread.interrupt();
        }
    }

}