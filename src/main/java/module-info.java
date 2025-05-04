module org.example.ooppr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens org.example.ooppr to javafx.fxml;
    exports org.example.ooppr;
    exports org.example.ooppr.ui.controllers;
    opens org.example.ooppr.ui.controllers to javafx.fxml;
}