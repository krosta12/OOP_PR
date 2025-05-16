package org.example.ooppr.core.network;

import org.example.ooppr.Server.Data;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.CanvasStateMessage;
import org.example.ooppr.core.network.protocol.DrawActionMessage;
import org.example.ooppr.core.network.protocol.InitUserMessage;
import org.example.ooppr.core.network.protocol.UndoMessage;
import org.example.ooppr.core.users.User;
import org.example.ooppr.ui.managers.PaintingZoneManager;

import java.io.*;
import java.net.*;

public class Client {


    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final PaintingZoneManager paintingZoneManager;

    public Client( PaintingZoneManager paintingZoneManager ) {
        this.paintingZoneManager = paintingZoneManager;
    }

    //WARN DOC
    public void connect(String ip, int port, User user ) {
        new Thread(() -> { //WARN RECHECK ARROW FUNC //WARN RECHECK MEMORY AFTER NEW OBJECT
            try {
                socket = new Socket(ip, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // First OUT message
                InitUserMessage initUserMsg = new InitUserMessage( user );
                out.writeObject( initUserMsg );
                out.flush();

                // First message - initialization
                Object msg = in.readObject();

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
                    }
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] Error: " + e.getMessage());
            }
        }).start(); //WARN CHECK IS SERVER STOP AFTER START
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

    public void undo( String nickname ) {
        try {
            UndoMessage msg = new UndoMessage( nickname );
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            System.out.println( "[CLIENT] UNDO exception: " + e.getMessage());
        }
    }
}