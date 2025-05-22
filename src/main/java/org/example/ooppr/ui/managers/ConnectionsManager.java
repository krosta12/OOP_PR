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

    /**
     * Sorts list of users for correct displaying
     * @return sorted list of users
     */
    private List<User> getSortedUsers() {
        return connectedUsers.stream()
                .sorted((u1, u2) -> {
                    int rolePriority1 = u1.getRolePriority();
                    int rolePriority2 = u2.getRolePriority();

                    if( rolePriority1 != rolePriority2 )
                        return Integer.compare( rolePriority1, rolePriority2 );
                    else
                        return u1.getConnectionTime().compareTo( u2.getConnectionTime() );
        }).collect( Collectors.toList() );
    }

}
