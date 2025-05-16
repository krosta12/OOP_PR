package org.example.ooppr.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import org.example.ooppr.core.users.User;

public class UserItemController {
    @FXML private Label userRoleLabel;
    @FXML private MenuItem kickUser;
    @FXML private MenuItem banUser;
    @FXML private MenuItem changeUserRole;
    @FXML private Label userNicknameLabel;

    private User user;

    public void setUser( User user ) {
        this.user = user;
        userNicknameLabel.setText( user.getNickname() );
        userRoleLabel.setText( "(" + user.getRole() + ")" );

        setupActions();
    }

    private void setupActions() {
        kickUser.setOnAction( e -> kickUser() );
    }

    private void kickUser() {
        // TODO kick
        System.out.println( "Kick user: " + user.getNickname() );
    }

    private void banUser() {
        // TODO ban
        System.out.println( "Ban user: " + user.getNickname() );
    }

    private void changeUserRole() {
        // TODO change role
        System.out.println( "Change role for user: " + user.getNickname() );
    }

}
