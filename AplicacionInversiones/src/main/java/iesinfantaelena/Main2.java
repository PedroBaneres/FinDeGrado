package iesinfantaelena;

import iesinfantaelena.controllers.LoginController;
import iesinfantaelena.controllers.MasterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main2 extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            MasterController masterController = new MasterController();
            masterController.start(stage);
        }

        public static void main(String[] args) {
            launch(args);
        }
    }