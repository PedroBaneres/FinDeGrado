module com.example.comunicacionentreventanas {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;


    opens com.controlador to javafx.fxml;
    exports com.controlador;
    exports com;
    opens com to javafx.fxml;
}