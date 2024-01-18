module com.example.comunicacionentreventanas {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;


    opens com.example.controlador to javafx.fxml;
    exports com.example.controlador;
}