package iesinfantaelena.controllers.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import iesinfantaelena.controllers.MasterController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class TransactionsController {
    MasterController masterController;
    @FXML
    private ListView transactionList;
    @FXML
    private FontAwesomeIconView backButton;

    @FXML
    private Label balance;
    @FXML
    private Button transferBtn;


    private void handleBackAction(MouseEvent event) throws SQLException, IOException {
        try {
            masterController.switchToHomepage(); // Asume que este método ya existe y funciona correctamente
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Maneja la excepción como consideres necesario
        }
    }
    @FXML
    public void initialize(MasterController masterController) throws SQLException {
        this.masterController = masterController;
        balance.setText(masterController.activeUser.getTotalBalance(masterController.getDatabaseConnection()) + "€");
        // Ahora que masterController está configurado, puedes configurar el manejador de eventos
        backButton.setOnMouseClicked(event -> {
            try {
                handleBackAction(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        populateListView();
    }
    public void openTransfer() throws IOException {
        masterController.showTransferMenu();

    }




    public void populateListView() {
        ObservableList<String> transactionDetails = FXCollections.observableArrayList();

        try {
            // Connect to the database
            Connection connection = masterController.getDatabaseConnection();

            // Prepare the SQL statement to fetch transactions
            String sql = "SELECT * FROM transactions WHERE source_IBAN IN (SELECT IBAN FROM accounts WHERE Username = ?) OR destination_IBAN IN (SELECT IBAN FROM accounts WHERE Username = ?) ORDER BY transaction_date DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, masterController.activeUser.getUsername());
            preparedStatement.setString(2, masterController.activeUser.getUsername());

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate over the result set and add transaction details to the ObservableList
            while (resultSet.next()) {
                // Extract transaction details
                String sourceIBAN = resultSet.getString("source_IBAN");
                String destinationIBAN = resultSet.getString("destination_IBAN");
                double amount = resultSet.getDouble("amount");
                String description = resultSet.getString("description");
                Date date = resultSet.getDate("transaction_date");

                // Determine if it's a top-up transaction
                boolean isTopUp = description.equalsIgnoreCase("Ingreso");

                // Determine if the current user is the sender or receiver
                boolean isSender = sourceIBAN.equals(masterController.activeUser.ibans.get(0));

                // Adjust the transaction amount based on the sender/receiver and top-up condition
                String formattedAmount;
                if (isTopUp) {
                    formattedAmount = "+" + amount;
                } else {
                    formattedAmount = isSender ? "-" + amount : "+" + amount;
                }

                // Determine the account IBAN to display
                String accountIBAN = isSender ? destinationIBAN : sourceIBAN;
                if (accountIBAN == null) {
                    accountIBAN = "";
                }

                // Format the transaction date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
                String formattedDate = dateFormat.format(date);

                // Add transaction details to the list
                transactionDetails.add(formattedDate + "    " + accountIBAN + "    " + formattedAmount + "    " + description);
            }

            // Set the populated list to the ListView
            transactionList.setItems(transactionDetails);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }




}
