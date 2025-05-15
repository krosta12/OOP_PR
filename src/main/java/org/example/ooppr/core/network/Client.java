package org.example.ooppr.core.network;

import org.example.ooppr.Server.Data;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.CanvasStateMessage;
import org.example.ooppr.core.network.protocol.DrawActionMessage;
import org.example.ooppr.ui.managers.PaintingZoneManager;

import java.io.*;
import java.net.*;

public class Client {


    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    //WARN DOC
    public static void connect(String ip, int port, PaintingZoneManager paintingZoneManager) {
        new Thread(() -> { //WARN RECHECK ARROW FUNC //WARN RECHECK MEMORY AFTER NEW OBJECT
            try ( Socket socket = new Socket(ip, port);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {

                Object msg = in.readObject();

                // First message - initialization
                if( msg instanceof CanvasStateMessage canvasMsg) {
                    paintingZoneManager.initializeCanvas(
                            canvasMsg.getxResolution(),
                            canvasMsg.getyResolution(),
                            canvasMsg.getColorWeb()
                    );

                    // drawing all by history
                    for( DrawAction action : canvasMsg.getDrawActions() ) {
                        paintingZoneManager.drawByDrawAction(action);
                    }
                } else if ( msg instanceof DrawActionMessage drawMsg) {
                    DrawAction action = drawMsg.getDrawAction();
                    paintingZoneManager.drawByDrawAction(action);
                } else {
                    System.out.println( "! Unknown message" );
                }

                while (true) { // getting all new DrawActions
                    DrawAction action = (DrawAction) in.readObject();
                    paintingZoneManager.drawByDrawAction(action);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
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
            System.out.println("! Failed to send message");
        }
    }
}