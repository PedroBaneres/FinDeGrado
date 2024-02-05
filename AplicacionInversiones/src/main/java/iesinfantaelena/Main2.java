package iesinfantaelena;

import iesinfantaelena.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        LoginController controladorAcceso = loader.getController();
        controladorAcceso.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}