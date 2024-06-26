package iesinfantaelena.bank;

import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChartManager {
    public ChartManager() {
    }

    public XYChart.Series<String, Number> getAccountBalanceDataLastWeek(Connection connection, String username) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();


        String sql = "SELECT DATE(transaction_date) AS transaction_day, source_IBAN_balanceAT " +
                "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY DATE(transaction_date) ORDER BY transaction_date DESC) AS rn " +
                "      FROM transactions " +
                "      WHERE DATE(transaction_date) >= DATE_SUB(CURDATE(), INTERVAL 1 WEEK) " +
                "      AND (source_IBAN IN (SELECT IBAN FROM accounts WHERE username = ?) OR destination_IBAN IN (SELECT IBAN FROM accounts WHERE username = ?))) AS ranked_transactions " +
                "WHERE rn = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String date = resultSet.getString("transaction_day");
                    double balanceAT = resultSet.getDouble("source_IBAN_balanceAT");
                    series.getData().add(new XYChart.Data<>(date, balanceAT));
                }
            }
        }

        return series;
    }
    public XYChart.Series<String, Number> getAccountBalanceDataAllTime(Connection connection, String username) throws SQLException {
        XYChart.Series<String, Number> series = new XYChart.Series<>();


        String sql = "SELECT DATE(transaction_date) AS transaction_day, source_IBAN_balanceAT " +
                "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY DATE(transaction_date) ORDER BY transaction_date DESC) AS rn " +
                "      FROM transactions " +
                "      WHERE (source_IBAN IN (SELECT IBAN FROM accounts WHERE username = ?) OR destination_IBAN IN (SELECT IBAN FROM accounts WHERE username = ?))) AS ranked_transactions " +
                "WHERE rn = 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("transaction_day");
                    double balanceAT = resultSet.getDouble("source_IBAN_balanceAT");
                    series.getData().add(new XYChart.Data<>(date, balanceAT));
                }
            }
        }

        return series;
    }





}
