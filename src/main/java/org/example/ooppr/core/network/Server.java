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
                Object data = in.readObject();
                if( data instanceof DrawAction ) {
                    history.add( (DrawAction) data);
                    broadcast( (DrawAction) data);
                } else if( data instanceof String ) {
                    if( data.equals("getResolution") ) {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeChars( this.xResolution + " x " + this.yResolution );
                    }
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println( "- Client disconnected: " );
        }
    }


    /**
     * Broadcasting all data to all Clients (echo)
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


    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }
}