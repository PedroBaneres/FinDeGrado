module com.example.AplicacionInversiones{
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
    exports iesinfantaelena.controllers.admin;
    opens iesinfantaelena.controllers.admin to javafx.fxml;
    exports iesinfantaelena.controllers.client;
    opens iesinfantaelena.controllers.client to javafx.fxml;
    exports iesinfantaelena.exceptions;
    opens iesinfantaelena.exceptions to javafx.fxml;
}