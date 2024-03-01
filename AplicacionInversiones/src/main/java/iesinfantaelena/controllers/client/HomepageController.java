package iesinfantaelena.controllers.client;

import iesinfantaelena.bank.ChartManager;
import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.Connection;
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
    private ImageView imageView;
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
        masterController.showSupportChat();
    }

    public void initialize(MasterController masterController) throws SQLException {
        labelNombre.setText("Bienvenido " + masterController.activeUser.getUsername());
        this.chartManager = new ChartManager();
        balanceLabel.setText(masterController.activeUser.getTotalBalance(masterController.getDatabaseConnection()) + "€");
        this.masterController= masterController;
        populateLastWeekChart();
    }
    public void initialize() {
        imageView.setOnMouseClicked(event -> {
            try {
                masterController.switchToTransactions();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void populateLastWeekChart() {
        try {
            // Get database connection
            Connection connection = masterController.getDatabaseConnection();

            // Get username of the logged-in user
            String username = masterController.activeUser.getUsername();

            // Get account balance data for the last week
            XYChart.Series<String, Number> series = chartManager.getAccountBalanceDataLastWeek(connection, username);

            // Add the series to the chart
            weekLineChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
