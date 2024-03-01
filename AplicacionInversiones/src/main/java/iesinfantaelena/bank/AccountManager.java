package iesinfantaelena.bank;

import iesinfantaelena.exceptions.InsufficientBalanceException;

import java.sql.*;

public class AccountManager {

    // Method to add money to an account and record the transaction
    public void addMoney(String IBAN, double amount, Connection connection) throws SQLException {
        try {
            // Begin transaction
            connection.setAutoCommit(false);

            // Add amount to account
            String addSql = "UPDATE accounts SET balance = balance + ? WHERE IBAN = ?";
            PreparedStatement addStatement = connection.prepareStatement(addSql);
            addStatement.setDouble(1, amount);
            addStatement.setString(2, IBAN);
            addStatement.executeUpdate();

            Double BalanceAT = fetchBalance(IBAN, connection);

            // Insert the transaction into the transactions table
            String insertSql = "INSERT INTO transactions (source_IBAN, transaction_date, amount, description, source_IBAN_balanceAT) VALUES (?, ?, ?, ?, ? )";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, IBAN); // Source IBAN
            insertStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertStatement.setDouble(3, amount);
            insertStatement.setString(4, "Ingreso");
            insertStatement.setDouble(5, BalanceAT); // BalanceAT of the source IBAN
            insertStatement.executeUpdate();

            // Commit the transaction
            connection.commit();
            connection.setAutoCommit(true);

            System.out.println("Successfully added " + amount + " to account " + IBAN);
        } catch (SQLException e) {
            // Rollback transaction in case of error
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
    }


    // Method to withdraw money from an account and record the transaction
    public void withdrawMoney(String IBAN, double amount, Connection connection) throws SQLException {
        try {
            // Begin transaction
            connection.setAutoCommit(false);
            // Check if there is enough balance to withdraw
            String checkBalanceSql = "SELECT balance FROM accounts WHERE IBAN = ?";
            PreparedStatement checkBalanceStatement = connection.prepareStatement(checkBalanceSql);
            checkBalanceStatement.setString(1, IBAN);
            ResultSet resultSet = checkBalanceStatement.executeQuery();
            if (resultSet.next()) {
                double saldo = resultSet.getDouble("balance");
                if (saldo < amount) {
                    throw new SQLException("Insufficient balance");

                }
            }

            // Deduct amount from account
            String deductSql = "UPDATE accounts SET balance = balance - ? WHERE IBAN = ?";
            PreparedStatement deductStatement = connection.prepareStatement(deductSql);
            deductStatement.setDouble(1, amount);
            deductStatement.setString(2, IBAN);
            deductStatement.executeUpdate();

            Double BalanceAT = fetchBalance(IBAN,connection);
            // Insert the transaction into the transactions table
            String insertSql = "INSERT INTO transactions (source_IBAN, transaction_date, amount, description,source_IBAN_balanceAT) VALUES (?, ?, ?, ?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, IBAN);
            insertStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertStatement.setDouble(3, amount); // Negative amount to represent withdrawal
            insertStatement.setString(4, "Retirada");
            insertStatement.setDouble(5,BalanceAT);
            insertStatement.executeUpdate();

            // Commit the transaction
            connection.commit();
            connection.setAutoCommit(true);

            System.out.println("Successfully withdrawn " + amount + " from account " + IBAN);
        } catch (SQLException e) {
            // Rollback transaction in case of error
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
    }


    // Method to perform a transfer between two accounts and record the transaction
    public void transfer(String sourceIBAN, String destinationIBAN, double amount, String description, Connection connection) throws SQLException, InsufficientBalanceException {
        try {
            connection.setAutoCommit(false);

            // Fetch balances before the transaction
            double sourceBalanceBefore = fetchBalance(sourceIBAN, connection);
            if (sourceBalanceBefore < amount) {
                throw new InsufficientBalanceException("Insufficient balance in the source account");
            }
            double destinationBalanceBefore = fetchBalance(destinationIBAN, connection);

            // Deduct amount from source account
            String deductSql = "UPDATE accounts SET balance = balance - ? WHERE IBAN = ?";
            PreparedStatement deductStatement = connection.prepareStatement(deductSql);
            deductStatement.setDouble(1, amount);
            deductStatement.setString(2, sourceIBAN);
            deductStatement.executeUpdate();

            // Add amount to destination account
            String addSql = "UPDATE accounts SET balance = balance + ? WHERE IBAN = ?";
            PreparedStatement addStatement = connection.prepareStatement(addSql);
            addStatement.setDouble(1, amount);
            addStatement.setString(2, destinationIBAN);
            addStatement.executeUpdate();

            // Insert the transaction into the transactions table
            String insertSql = "INSERT INTO transactions (source_IBAN, destination_IBAN, transaction_date, amount, description, source_IBAN_balanceAT, destination_IBAN_balanceAT) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, sourceIBAN);
            insertStatement.setString(2, destinationIBAN);
            insertStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertStatement.setDouble(4, amount);
            insertStatement.setString(5, description);
            insertStatement.setDouble(6, sourceBalanceBefore - amount); // Source account balance after transaction
            insertStatement.setDouble(7, destinationBalanceBefore + amount); // Destination account balance after transaction
            insertStatement.executeUpdate();

            // Commit the transaction
            connection.commit();
            connection.setAutoCommit(true);

            System.out.println("Transfer of " + amount + " successfully made from account " + sourceIBAN + " to account " + destinationIBAN);
        } catch (SQLException e) {
            // Rollback transaction in case of error
            connection.rollback();
            connection.setAutoCommit(true);
            throw e;
        }
    }

    private double fetchBalance(String IBAN, Connection connection) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE IBAN = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, IBAN);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getDouble("balance");
        } else {
            throw new SQLException("Account with IBAN " + IBAN + " not found.");
        }
    }
}