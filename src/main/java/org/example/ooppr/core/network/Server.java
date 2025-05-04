package org.example.ooppr.core.network;

import javafx.scene.paint.Color;
import org.example.ooppr.Server.Data;
import org.example.ooppr.core.drawing.DrawAction;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

//WARN SERVER MUST BE CLOSED AFTER APP CLOUSING

public class Server {
    private int port;
    private String ip;
    private boolean isStarted = false;
    private final Set<ObjectOutputStream> clientStreams = ConcurrentHashMap.newKeySet();
    private final List<DrawAction> history = new ArrayList<>();


    // Canvas main parameters
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
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("* Server started at " + ip + ":" + port);

            while (isStarted) {
                Socket socket = serverSocket.accept();
                System.out.println("+ Client connected: " + socket.getRemoteSocketAddress());
                new Thread(() -> handleClient(socket) ).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket socket) {
        try( ObjectInputStream in = new ObjectInputStream(socket.getInputStream()) ) {
            while(true) {
                DrawAction action = (DrawAction) in.readObject();
                history.add(action);
                broadcast(action);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println( "- Client disconnected: " );
        }
    }


    /**
     * Broadcasting all data to all Clients (echo)
     * @param data Sending data
     */
    private void broadcast(DrawAction action) {
        for (ObjectOutputStream clientStream : clientStreams) {
            try {
                clientStream.writeObject(action);
                clientStream.flush();
            } catch (IOException e) {
                System.out.println("Data sending error: " + e.getMessage());
            }
        }
    }

    /**
     * @return String "[ip]:[port]"
     */
    public String getIpPort() {
        return this.ip + ":" + port;
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