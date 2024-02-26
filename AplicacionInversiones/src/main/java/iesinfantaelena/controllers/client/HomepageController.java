package iesinfantaelena.controllers.client;

import iesinfantaelena.bank.ChartManager;
import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class HomepageController {
    private MasterController masterController;
    private ChartManager chartManager;
    @FXML
    private Label labelNombre;
    @FXML
    private Label balanceLabel;
    @FXML
    private LineChart<String, Number> weekLineChart;
    @FXML
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
    }
    @FXML
    public void goToSettings(ActionEvent event) throws IOException {
        masterController.switchToSettings();
    }
    @FXML
    public void goToSupport(ActionEvent event) throws IOException{
        masterController.switchToSupportChat();
    }

    public void initialize(MasterController masterController) throws SQLException {
        labelNombre.setText("Bienvenido " + masterController.activeUser.getUsername());
        this.chartManager = new ChartManager();
        balanceLabel.setText(masterController.activeUser.getTotalBalance(masterController.getDatabaseConnection()) + "€");
        XYChart.Series<String, Number> accountBalance = chartManager.getAccountBalanceDataLastWeek(masterController.getDatabaseConnection(),masterController.activeUser.getUsername());
        this.masterController= masterController;
    }
}