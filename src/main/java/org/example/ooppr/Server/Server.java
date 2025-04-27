package org.example.ooppr.Server;

import javafx.scene.paint.Color;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

//WARN SERVER MUST BE CLOSED AFTER APP CLOUSING

public class Server {
    private int port;
    private boolean isStarted = false;
    private final Set<ObjectOutputStream> clientStreams = ConcurrentHashMap.newKeySet();

    // Добавленные поля для параметров холста
    private int xResolution;
    private int yResolution;
    private Color canvasColor;

    //WARN WRITE A DOC
    public Server(int port, int xResolution, int yResolution, Color canvasColor) {
        this.port = port;
        this.xResolution = xResolution;
        this.yResolution = yResolution;
        this.canvasColor = canvasColor;
    }
    //WARN WRITE A DOC
    public void startHost() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            isStarted = true;
            System.out.println("server started at " + InetAddress.getLocalHost().getHostAddress() + ":" + port);

            while (isStarted) {
                Socket socket = serverSocket.accept();
                System.out.println("new client connected: " + socket.getRemoteSocketAddress());
                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //WARN MOVE IT TO SECOND FILE
    private class ClientHandler implements Runnable {
        private Socket socket;

        //WARN WRITE DOC
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        //WARN WRITE DOC
        public void run() {
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                clientStreams.add(out);

                //WARN USE CONST-s?
                Map<String, Object> canvasParams = new HashMap<>();
                canvasParams.put("xResolution", xResolution);
                canvasParams.put("yResolution", yResolution);
                canvasParams.put("red", canvasColor.getRed());
                canvasParams.put("green", canvasColor.getGreen());
                canvasParams.put("blue", canvasColor.getBlue());
                canvasParams.put("opacity", canvasColor.getOpacity());
                out.writeObject(canvasParams);
                out.flush();

                Object obj;
                while ((obj = in.readObject()) != null) {
                    if (obj instanceof Data data) { //WARN TRANSLATE IT BEFORE RELEASE
                        System.out.println("Получена Data от клиента: x=" + data.getX() + ", y=" + data.getY());
                        broadcast(data);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {//WARN TRANSLATE
                System.out.println("Клиент отключился: " + e.getMessage());
            } finally {
                if (out != null) {
                    clientStreams.remove(out);
                    try { out.close(); } catch (IOException ignored) {}
                }
                try {
                    if (in != null) in.close();
                    socket.close();
                } catch (IOException ignored) {} //WARN CHECK IS SERVER CLOSED AFTER CRASH
            }
        }
    }

    //WARN DOC
    private void broadcast(Data data) {
        for (ObjectOutputStream out : clientStreams) {
            try {
                out.writeObject(data);
                out.flush();
            } catch (IOException e) { //WARN TRANSLATE
                System.out.println("Ошибка при отправке клиенту: " + e.getMessage());
            }
        }
    }

    //WARN DOC
    //WARN REVIEW USAGE
    public void connectToHost(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);//WARN TRANSLATE
            System.out.println("Подключен к серверу: " + ip + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}