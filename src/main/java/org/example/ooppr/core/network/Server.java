package org.example.ooppr.core.network;

import javafx.scene.paint.Color;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.*;
import org.example.ooppr.core.users.User;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

//WARN SERVER MUST BE CLOSED AFTER APP CLOUSING

public class Server {
    private int port;
    private String ip;
    private boolean isStarted = false;
    private final Map<User, ObjectOutputStream> users = new HashMap<>();

    private final List<DrawAction> history = new ArrayList<>();


    // Canvas main parameters
    private int xResolution;
    private int yResolution;
    private Color canvasColor;

    // User roles
    private User creator;

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
            System.out.println("* Server started at " + ip + ":" + port + " *");

            while (isStarted) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket) ).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Initializing user
            Object initUser = in.readObject();
            if( initUser instanceof InitUserMessage initUserMsg ){
                User user = initUserMsg.getUser();
                users.put( user, out );
                System.out.println("[SERVER] Client connected: " +
                        socket.getRemoteSocketAddress().toString().substring(1) + " " +
                        user.getNickname() );
            }
            // Send user list message
            UserConnectedMessage userConnectedMessage = new UserConnectedMessage( users.keySet().stream().toList() );
            broadcast( userConnectedMessage );

            // Send init data: actions, resolution and bc color
            CanvasStateMessage init = new CanvasStateMessage(history, xResolution, yResolution, canvasColor.toString());
            out.writeObject(init);
            out.flush();

            // Listening client
            while(true) {

                Object msg = in.readObject();

                if( msg instanceof DrawActionMessage drawMsg ) {
                    handleDrawActionMessage( drawMsg );
                } else if ( msg instanceof UndoMessage undoMsg ) {
                    handleUndoMessage( undoMsg );
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println( "[SERVER] Client disconnected: " );
        }
    }

    private void handleDrawActionMessage( DrawActionMessage drawMsg ) {
        DrawAction action = drawMsg.getDrawAction();
        history.add(action);
        broadcast( drawMsg );
    }

    private void handleUndoMessage( UndoMessage undoMsg ) {
        //broadcast( undoMsg );
    }


    /**
     * Broadcasting all data to all Clients (echo)
     */
    private void broadcast(Message msg) {
        for ( User client : users.keySet()) {
            try {
                users.get(client).writeObject(msg);
                users.get(client).flush();
                System.out.println( "[SERVER] broadcast to " + client.getNickname() );
            } catch (IOException e) {
                System.out.println("[SERVER] !Broadcast error: " + e.getMessage());
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

    public void setCreator(User creatorUser) {
        this.creator = creatorUser;
    }
}