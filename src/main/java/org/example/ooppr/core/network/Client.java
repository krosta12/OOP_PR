package org.example.ooppr.core.network;

import org.example.ooppr.core.ClientEventListener;
import org.example.ooppr.core.drawing.DrawAction;
import org.example.ooppr.core.network.protocol.*;
import org.example.ooppr.core.users.PriorityException;
import org.example.ooppr.core.users.User;

import java.io.*;
import java.net.*;
import java.util.List;

public class Client {


    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Thread clientThread;

    private ClientEventListener listener;

    public Client() {
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

                // Get connected users list or stop connection if name is not unique
                Object msg = in.readObject();
                if( msg instanceof UserConnectedMessage userConnectedMessage ) {
                    listener.onNewUsersList( userConnectedMessage.getNewUsersList() );
                } else if ( msg instanceof ExceptionMessage eMsg ) {
                    listener.onNicknameNotUnique();
                    return;
                }

                // Canvas initialization
                msg = in.readObject();

                // Init canvas
                if( msg instanceof CanvasStateMessage canvasMsg) {
                    handleCanvasStateMessage( canvasMsg );
                }

                while (true) { // getting all new messages
                    System.out.print( "[CLIENT] Received new message: " );

                    Object data = in.readObject();

                    if( data instanceof DrawActionMessage drawMsg ) {
                        System.out.println( "DrawActionMessage" );
                        handleDrawActionMessage( drawMsg );

                    } else if ( data instanceof UndoMessage undoMsg ) {
                        System.out.println( "UndoMessage" );
                        handleUndoMessage( undoMsg );
                    } else if ( data instanceof UserConnectedMessage userConnectedMessage) {
                        // show toast message?
                        System.out.println( "UserConnectedMessage" );
                        handleUserConnectedMessage( userConnectedMessage );
                    } else if ( data instanceof UserDisconnectedMessage userDisconnectedMessage ) {
                        // show toast message?
                        System.out.println( "UserDisconnectedMessage" );
                        handleUserDisconnectedMessage( userDisconnectedMessage );
                    } else if ( data instanceof KickUserMessage kickUserMessage ) {
                        System.out.println( "KickUserMessage" );
                        handleKickUserMessage( kickUserMessage );
                    }
                }
            } catch (Exception e) {
                System.out.println("[CLIENT] Error: " + e.getMessage());
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }

    // -- MESSAGE HANDLERS --
    private void handleCanvasStateMessage( CanvasStateMessage canvasStateMessage ) {
        int xRes = canvasStateMessage.getxResolution();
        int yRes = canvasStateMessage.getyResolution();
        String colorWeb = canvasStateMessage.getColorWeb();
        List<DrawAction> actions = canvasStateMessage.getDrawActions();
        listener.onInitializeCanvas( xRes, yRes, colorWeb, actions );
    }

    private void handleDrawActionMessage( DrawActionMessage drawActionMessage ) {
        DrawAction action = drawActionMessage.getDrawAction();
        User sender = drawActionMessage.getSender();
        listener.onDrawActionMessageReceived( action, sender );
    }

    private void handleUndoMessage( UndoMessage undoMessage ) {
        listener.onUndo();
//        paintingZoneManager.undoLastAction( undoMsg.getNickname() );
    }

    private void handleUserConnectedMessage( UserConnectedMessage userConnectedMessage ) {
        List<User> newUsersList = userConnectedMessage.getNewUsersList();
        listener.onNewUsersList( newUsersList );
    }

    private void handleUserDisconnectedMessage( UserDisconnectedMessage userDisconnectedMessage ) {
        List<User> newUsersList = userDisconnectedMessage.getNewUsersList();
        listener.onNewUsersList( newUsersList );

    }

    private void handleKickUserMessage( KickUserMessage kickUserMessage ) {
        listener.onKick();
    }

    //WARN DOC
    public void sendDrawAction(DrawAction action, User user) {
        try {
            DrawActionMessage msg = new DrawActionMessage(action, user);
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

    /**
     * Methods sends to server kick message. Also checks users priorities
     * @param itemUser
     * @param productUser
     * @throws PriorityException
     */
    public void kickUser( User itemUser, User productUser ) throws PriorityException {

        // checking priorities
        int itemUserRolePriority = itemUser.getRolePriority();
        int productUserRolePriority = productUser.getRolePriority();

        if( productUserRolePriority > 1 )
            throw new PriorityException("You can't kick " + itemUser.getNickname() + ". You don't have enough rights!");
        if( itemUser.getNickname().equals( productUser.getNickname() ) )
            throw new PriorityException( "You can't kick yourself!" );
        if( itemUserRolePriority == productUserRolePriority )
            throw new PriorityException( "You can't kick " + itemUser.getNickname() + ". You have the same priority level!" );

        // sending kick request to server
        sendKickMessage( itemUser, productUser );
    }

    /**
     * Method sends kicking message to server
     * @param kickee user who need to be kicked
     * @param kicker user who kicks keckee
     */
    private void sendKickMessage( User kickee, User kicker ) {
        try {
            KickUserMessage kickUserMessage = new KickUserMessage( kickee, kicker );
            out.writeObject( kickUserMessage );
            out.flush();
        } catch ( IOException e ) {
            System.out.println( "[CLIENT] Error sending kick message: " + e.getMessage() );
        }

    }

    public void setClientEventListener( ClientEventListener listener ) {
        this.listener = listener;
    }

}