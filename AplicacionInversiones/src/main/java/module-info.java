module com.example.comunicacionentreventanas {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;



    opens iesinfantaelena.controllers to javafx.fxml;
    exports iesinfantaelena.controllers;
    exports iesinfantaelena;
    opens iesinfantaelena to javafx.fxml;
}