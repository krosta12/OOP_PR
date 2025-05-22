module org.example.ooppr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens org.example.ooppr to javafx.fxml;
    opens org.example.ooppr.ui.controllers to javafx.fxml;
    exports org.example.ooppr;
    exports org.example.ooppr.ui.controllers;
}