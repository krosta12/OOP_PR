package org.example.ooppr;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.example.ooppr.Server.Server;

import java.io.IOException;

public class ServerOptionsController {
    // Создание сервера на порту 1234
    final Server server = new Server(1234);

    // Метод, вызываемый при нажатии на кнопку "Host"
    public void host(ActionEvent event) {
        // Запускаем сервер в отдельном потоке, чтобы не блокировать JavaFX UI
        new Thread(server::start_host).start();

        try {
            // Загружаем новую сцену из FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ooppr/CreationInterfaceV2.fxml"));
            Parent root = fxmlLoader.load();

            // Получаем текущий Stage и меняем корневой элемент сцены
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void join() {
        System.out.println("join");
    }
}
