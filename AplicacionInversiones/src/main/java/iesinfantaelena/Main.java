package iesinfantaelena;

import iesinfantaelena.bank.AccountManager;
import iesinfantaelena.controllers.MasterController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        MasterController masterController = new MasterController();
        AccountManager transferenciaManager = new AccountManager();
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bank.png")));
        stage.getIcons().add(icon);
        //transferenciaManager.transfer("ES-000003","ES-000001",120,"Prueba",masterController.getDatabaseConnection());
        transferenciaManager.addMoney("ES-000004",1000, masterController.getDatabaseConnection());
        //transferenciaManager.withdrawMoney("ES-000001",400, masterController.getDatabaseConnection());
        masterController.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}