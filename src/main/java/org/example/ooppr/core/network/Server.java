package org.example.ooppr.core.network;

import javafx.scene.paint.Color;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.*;
import org.example.ooppr.core.users.PriorityException;
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
    private final Map<User, ObjectOutputStream> users = new ConcurrentHashMap<>();
    private ServerSocket serverSocket;
    private Set<String> bannedIps = new HashSet<>();

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
        try {
            serverSocket = new ServerSocket(this.port);
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

    public void stop() {
        try {
            isStarted = false;

            for ( User u : users.keySet() ) {
                KickUserMessage kickUserMessage = new KickUserMessage( u, creator );
                ObjectOutputStream out = users.get( u );
                out.writeObject( kickUserMessage );
                out.flush();
            }

            if( serverSocket != null && !serverSocket.isClosed() )
                serverSocket.close();
            System.out.println( "[SERVER] stopped" );


        } catch ( IOException e ) {
            System.out.println( "[SERVER] Error closing socket: " + e.getMessage() );
        }
    }

    private void handleClient(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String clientIp = socket.getInetAddress().getHostAddress();

            // Initializing user
            Object initUser = in.readObject();
            if( initUser instanceof InitUserMessage initUserMsg ){
                User newUser = initUserMsg.getUser();

                // Checking ban
                if( bannedIps.contains( clientIp ) ) {
                    UserBannedException e = new UserBannedException( "User is banned!" );
                    ExceptionMessage eMsg = new ExceptionMessage( e );
                    out.writeObject( eMsg );
                    out.flush();
                    return;
                }

                // Checking nickname uniqueness
                for( User u : users.keySet() ) {
                    if( u.getNickname().equals( newUser.getNickname() ) ) {
                        NotUniqueNicknameException e = new NotUniqueNicknameException("User nickname is not unique!");
                        ExceptionMessage eMsg = new ExceptionMessage( e );
                        out.writeObject( eMsg );
                        out.flush();
                        return;
                    }
                }

                users.put( newUser, out );
                System.out.println("[SERVER] Client connected: " +
                        socket.getRemoteSocketAddress().toString().substring(1) + " " +
                        newUser.getNickname() );
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
                } else if( msg instanceof DisconnectMessage disconnectMessage ) {
                    handleDisconnectMessage( disconnectMessage );
                } else if( msg instanceof KickUserMessage kickUserMessage ) {
                    handleKickUserMessage( kickUserMessage );
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println( "[SERVER] Client disconnected: " );
        }
    }

    // -- HANDLER METHODS --
    // <editor-fold desc="Handler Methods">

    private void handleDrawActionMessage( DrawActionMessage drawMsg ) {
        DrawAction action = drawMsg.getDrawAction();
        history.add(action);
        broadcast( drawMsg );
    }

    /**
     * The method handles DisconnectMessage
     * @param disconnectMessage
     */
    private void handleDisconnectMessage( DisconnectMessage disconnectMessage ) {
        for( User u : users.keySet() ) { // checking disconnected user nickname and removing it from all users list
            if( u.getNickname().equals( disconnectMessage.getUser().getNickname() ) ) {
                users.remove( u );
            }
        }
        // broadcast to other users
        broadcast( new UserDisconnectedMessage( users.keySet().stream().toList() ) );
    }

    /**
     * Double-checks users roles priorities and sends message to kickee
     */
    private void handleKickUserMessage( KickUserMessage kickUserMessage ) {
        User kickee = kickUserMessage.gerKickee();
        User kicker = kickUserMessage.getKicker();
        if( canKick( kickee, kicker ) ) {
            for( User u : users.keySet() ) { // watching all users
                try {
                    if( u.getNickname().equals( kickee.getNickname() ) ) { // find needed user by nickname
                        users.get( u ).writeObject( kickUserMessage );
                        users.get( u ).flush();
                    }
                } catch ( IOException e ) {
                    System.out.println( "[SERVER] Can't send kick message to client: " + e.getMessage() );
                }

            }
        }
    }

    private void handleUndoMessage( UndoMessage undoMsg ) {
        //broadcast( undoMsg );
    }

    // </editor-fold>


    // -- OTHER VERY IMPORTANT METHODS --
    // <editor-fold desc="Other very important methods">

    /**
     * The method checks does user kicker can kick user kickee
     * @return boolean can kick or not
     */
    private boolean canKick( User kickee, User kicker ) {
        if( kicker.getRolePriority() > 1 )
            return false;
        if( kickee.getNickname().equals( kicker.getNickname() ) )
            return false;
        if( kickee.getRolePriority() == kicker.getRolePriority() )
            return false;
        return true;
    }

    /**
     * Broadcasting all data to all Clients (echo)
     */
    private void broadcast(Message msg) {

        List<User> disconnected = new ArrayList<>();

        for ( User client : users.keySet()) {
            try {
                users.get(client).writeObject(msg);
                users.get(client).flush();
                System.out.println( "[SERVER] broadcast to " + client.getNickname() );
            } catch (IOException e) {
                System.out.println("[SERVER] !Broadcast error to " + client.getNickname() + ": " + e.getMessage());
                disconnected.add( client );

            }
        }

        for( User client : disconnected ) {
            users.remove( client );
            System.out.println( "[SERVER] Removed disconnected user: " + client.getNickname() );
        }
    }

    // </editor-fold>

    // -- GETTERS AND SETTERS --
    // <editor-fold desc="Getters and Setters">

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setCreator(User creatorUser) {
        this.creator = creatorUser;
    }
    // </editor-fold>
}