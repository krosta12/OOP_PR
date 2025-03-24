module org.example.ooppr {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ooppr to javafx.fxml;
    exports org.example.ooppr;
}