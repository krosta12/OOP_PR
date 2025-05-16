package org.example.ooppr.ui.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.example.ooppr.core.users.User;
import org.example.ooppr.ui.controllers.UserItemController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionsManager {

    private final VBox userListVBox;
    private final List<User> connectedUsers = new ArrayList<>();

    public ConnectionsManager(VBox userListVBox) {
        this.userListVBox = userListVBox;
    }

    /**
     * Method adds user FXML item to VBox
     * @param user user object
     */
    public void addUser( User user )  {
        connectedUsers.add( user );
        refreshList();
    }

    public List<User> getConnectedUsers() {
        return connectedUsers;
    }

    public void removeUser( User user ) {
        connectedUsers.remove( user );
        refreshList();
    }

    /**
     * Refreshes users list
     */
    private void refreshList() {
        userListVBox.getChildren().clear();
        for( User user : getSortedUsers() ) {
            try {
                FXMLLoader loader = new FXMLLoader( getClass().getResource("/org/example/ooppr/UserItem.fxml") );
                Node userNode = loader.load();

                UserItemController controller = loader.getController();
                controller.setUser( user );
                userListVBox.getChildren().add( userNode );

            } catch ( IOException e ) {
                System.out.println( "Error adding user: " + e.getMessage() );
            }
        }
    }

    private List<User> getSortedUsers() {
        return connectedUsers.stream()
                .sorted((u1, u2) -> {
                    int rolePriority1 = getRolePriority(u1.getRole());
                    int rolePriority2 = getRolePriority(u2.getRole());

                    if( rolePriority1 != rolePriority2 )
                        return Integer.compare( rolePriority1, rolePriority2 );
                    else
                        return u1.getConnectionTime().compareTo( u2.getConnectionTime() );
        }).collect( Collectors.toList() );
    }

    /**
     * Filtering roles by priority (CREATOR first, then ADMIN, EDIT, VIEW_ONLY and other)
     * @param role Role
     * @return Priority value
     */
    private int getRolePriority( User.Role role ) {
        if ( role == null ) return 999;

        return switch( role ) {
            case CREATOR -> 0;
            case ADMIN -> 1;
            case EDIT -> 2;
            case VIEW_ONLY -> 3;
            default -> 999;
        };


    }

}
