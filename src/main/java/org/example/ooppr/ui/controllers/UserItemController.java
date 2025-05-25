package org.example.ooppr.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ooppr.core.network.Client;
import org.example.ooppr.core.users.PriorityException;
import org.example.ooppr.core.users.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserItemController {
    @FXML private Label userRoleLabel;
    @FXML private MenuItem kickUser;
    @FXML private MenuItem banUser;
    @FXML private MenuItem changeUserRole;
    @FXML private Label userNicknameLabel;

    private User itemUser;
    private User productUser;
    private Client client;

    public void setUser( User itemUser, User productUser, Client client ) {
        this.itemUser = itemUser;
        this.productUser = productUser;
        this.client = client;
        userNicknameLabel.setText( itemUser.getNickname() );
        userRoleLabel.setText( "(" + itemUser.getRole() + ")" + ( (itemUser.getNickname().equals(productUser.getNickname())) ? " [YOU]" : "") );

        setupActions();
    }

    private void setupActions() {
        kickUser.setOnAction( e -> kickUser() );
        banUser.setOnAction( e -> banUser() );
        changeUserRole.setOnAction( e -> changeUserRole() );
    }

    private void kickUser() {

        try{
            client.kickUser( itemUser, productUser );
            System.out.println( productUser.getNickname() + " kicked user " + itemUser.getNickname() );
        } catch (PriorityException e) {
            showNotEnoughPermissionAlert( e.getMessage() );
        }
    }

    private void banUser() {
        try{
            client.banUser( itemUser, productUser );
            System.out.println( productUser.getNickname() + " banned user " + itemUser.getNickname() );
        } catch (PriorityException e) {
            showNotEnoughPermissionAlert( e.getMessage() );
        }
    }

    private void changeUserRole() {
        try{
            User.Role newRole = askNewRoleAlert();
            System.out.println( "new role: " + newRole );
            client.changeUserRole( itemUser, productUser, newRole );
            System.out.println( productUser.getNickname() + " changed role for " + itemUser.getNickname() );
        } catch (PriorityException e) {
            showNotEnoughPermissionAlert( e.getMessage() );
        }
    }

    private User.Role askNewRoleAlert() {
        List<User.Role> choices = Arrays.asList(
                User.Role.ADMIN,
                User.Role.VIEW_ONLY,
                User.Role.EDIT
        );

        ChoiceDialog<User.Role> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Change User Role");
        dialog.setHeaderText("Select new role for the user:");
        dialog.setContentText("Role:");

        Optional<User.Role> result = dialog.showAndWait();
        return result.orElse(null);

    }

    private void showNotEnoughPermissionAlert( String msg ) {

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/ooppr/angry-smile.png")));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Not enough permission");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setGraphic(imageView);
        alert.showAndWait();

    }

}
