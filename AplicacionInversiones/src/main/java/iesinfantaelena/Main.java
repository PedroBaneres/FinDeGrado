package iesinfantaelena;

import iesinfantaelena.bank.AccountManager;
import iesinfantaelena.controllers.MasterController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        MasterController masterController = new MasterController();
        AccountManager transferenciaManager = new AccountManager();
        //transferenciaManager.transfer("ES-000003","ES-000001",120,"Prueba",masterController.getDatabaseConnection());
        //transferenciaManager.addMoney("ES-000001",1000, masterController.getDatabaseConnection());
        //transferenciaManager.withdrawMoney("ES-000001",400, masterController.getDatabaseConnection());
        masterController.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}