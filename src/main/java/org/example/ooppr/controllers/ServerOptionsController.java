package org.example.ooppr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerOptionsController {
    // Метод, вызываемый при нажатии на кнопку "Host" + УБРАТЬ active флагв из IP инпута, вероятно переменить на порт
    public void host(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ooppr/CreationInterfaceHost.fxml"));
            Parent root = fxmlLoader.load();

            // Получаем контроллер
            CreationInterfaceHostController controller = fxmlLoader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void join(ActionEvent event) { // меняем join() чтобы он тоже принимал ActionEvent
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ooppr/JoinRoom.fxml"));
            Parent root = fxmlLoader.load();

            // Получаем контроллер
            JoinRoomController controller = fxmlLoader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
