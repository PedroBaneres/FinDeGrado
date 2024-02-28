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
        String sql = "SELECT transaction_date, source_IBAN_balanceAT " +
                "FROM ( " +
                "   SELECT transaction_date, source_IBAN_balanceAT " +
                "   FROM transactions " +
                "   WHERE transaction_date >= DATE_SUB(NOW(), INTERVAL 1 WEEK) " +
                "   AND source_IBAN IN ( " +
                "       SELECT IBAN FROM accounts WHERE username = ? " +
                "   ) " +
                "   UNION ALL " +
                "   SELECT transaction_date, destination_IBAN_balanceAT " +
                "   FROM transactions " +
                "   WHERE transaction_date >= DATE_SUB(NOW(), INTERVAL 1 WEEK) " +
                "   AND destination_IBAN IN ( " +
                "       SELECT IBAN FROM accounts WHERE username = ? " +
                "   ) " +
                ") AS transactions " +
                "ORDER BY transaction_date";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, username);
        ResultSet resultSet = statement.executeQuery();

        // Process retrieved data and add it to the series
        while (resultSet.next()) {
            String date = resultSet.getString("transaction_date");
            double balanceAT = resultSet.getDouble("source_IBAN_balanceAT");
            series.getData().add(new XYChart.Data<>(date, balanceAT));
        }
        return series;
    }


}
