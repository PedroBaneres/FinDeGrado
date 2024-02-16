package iesinfantaelena;

import iesinfantaelena.controllers.MasterController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MasterController masterController = new MasterController();
        masterController.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}