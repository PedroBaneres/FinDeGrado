package iesinfantaelena.bank;

import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartManager {
    public ChartManager() {
    }

    // Method to retrieve account balance data for the last week
    public XYChart.Series<String, Number> getAccountBalanceDataLastWeek(Connection connection, String username) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        // Query to retrieve account balance data for the last week
        String sql = "SELECT t.transaction_date, a.balance " +
                "FROM transactions t " +
                "JOIN accounts a ON t.source_IBAN = a.IBAN " +
                "WHERE t.transaction_date >= DATE_SUB(NOW(), INTERVAL 1 WEEK) " +
                "AND a.username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        // Process retrieved data and add it to the series
        while (resultSet.next()) {
            String date = resultSet.getString("transaction_date");
            double balance = resultSet.getDouble("balance");
            series.getData().add(new XYChart.Data<>(date, balance));
        }
        return series;
    }
}
