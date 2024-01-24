package com;

import com.controlador.ControladorAcceso;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/controlador/ventanaAcceso.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        ControladorAcceso controladorAcceso = loader.getController();
        controladorAcceso.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}