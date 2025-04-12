module org.example.ooppr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.ooppr to javafx.fxml;
    exports org.example.ooppr;
}