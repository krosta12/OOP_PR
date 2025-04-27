package org.example.ooppr.Server;

import javafx.scene.paint.Color;
import java.io.*;
import java.net.*;
import java.util.Map;

public class Client {
    public interface CanvasParamsCallback {
        void onCanvasParamsReceived(int xRes, int yRes, Color color);
        void onConnectionError(String message);
    }

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    //WARN DOC
    public static void connect(String ip, int port, CanvasParamsCallback callback) {
        new Thread(() -> { //WARN RECHECK ARROW FUNC //WARN RECHECK MEMORY AFTER NEW OBJECT
            try (Socket socket = new Socket(ip, port);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                Object initialData = in.readObject();
                if (initialData instanceof Map) {
                    Map<?, ?> params = (Map<?, ?>) initialData;
                    int xRes = (int) params.get("xResolution");
                    int yRes = (int) params.get("yResolution");
                    double red = (double) params.get("red");
                    double green = (double) params.get("green");
                    double blue = (double) params.get("blue");
                    double opacity = (double) params.get("opacity");

                    callback.onCanvasParamsReceived(xRes, yRes,
                            new Color(red, green, blue, opacity));
                }

                while (true) {
                    Object data = in.readObject();
                    if (data instanceof Data) {
                        // WARN CREATE A DRAW LOGIC
                    }
                }
            } catch (Exception e) {
                callback.onConnectionError(e.getMessage());
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