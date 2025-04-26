package org.example.ooppr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.ooppr.Server.Server;
import org.example.ooppr.controllers.CreationInterfaceController;

import java.io.IOException;

public class ServerOptionsController {
    // Метод, вызываемый при нажатии на кнопку "Host" + УБРАТЬ active флагв из IP инпута, вероятно переменить на порт
    public void host(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ooppr/CreationInterfaceV2.fxml"));
            Parent root = fxmlLoader.load();

            // Получаем контроллер
            CreationInterfaceController controller = fxmlLoader.getController();
            controller.setConnectionType("host"); // Передаем "host"

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void join(ActionEvent event) { // меняем join() чтобы он тоже принимал ActionEvent
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ooppr/CreationInterfaceV2.fxml"));
            Parent root = fxmlLoader.load();

            // Получаем контроллер
            CreationInterfaceController controller = fxmlLoader.getController();
            controller.setConnectionType("join"); // Передаем "join"

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
