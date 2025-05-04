package org.example.ooppr.core.network;

import javafx.scene.paint.Color;
import org.example.ooppr.Server.Data;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.ui.managers.PaintingZoneManager;

import java.io.*;
import java.net.*;

public class Client {
    public interface CanvasParamsCallback {
        void onCanvasParamsReceived(int xRes, int yRes, Color color);
        void onConnectionError(String message);
    }

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    //WARN DOC
    public static void connect(String ip, int port, PaintingZoneManager paintingZoneManager) {
        new Thread(() -> { //WARN RECHECK ARROW FUNC //WARN RECHECK MEMORY AFTER NEW OBJECT
            try (Socket socket = new Socket(ip, port);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                Object firstAction = in.readObject();

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
    public void sendData(Data data) {
        try {
            out.writeObject(data);//WARN REHECK USAGES
            out.flush();
        } catch (IOException e) { //WARN TRANSLATE
            System.out.println("Не удалось отправить данные серверу.");
        }
    }
}