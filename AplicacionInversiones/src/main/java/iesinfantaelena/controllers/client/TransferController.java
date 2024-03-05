package iesinfantaelena.controllers.client;

import iesinfantaelena.User;
import iesinfantaelena.bank.AccountManager;
import iesinfantaelena.exceptions.InsufficientBalanceException;
import iesinfantaelena.controllers.MasterController;
import iesinfantaelena.exceptions.DatabaseConnectionException;
import iesinfantaelena.exceptions.UserNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.SQLException;

public class TransferController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField subjectTextField;
    private MasterController masterController;
    private AccountManager accountManager;
    private Stage stage;

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        accountManager = new AccountManager();
        this.stage = masterController.getStage();
    }

    @FXML
    public void sendMoney() throws SQLException {
        String username = usernameTextField.getText();
        double amount = 0;
        try {
            amount = Double.parseDouble(quantityTextField.getText());
            if(amount <= 0){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor introduzca un número válido", "Error", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method if the amount is not valid
        }

        // Check if the destination username is the same as the active user's username
        if (username.equals(masterController.activeUser.getUsername())) {
            JOptionPane.showMessageDialog(null, "No puede enviar dinero.jpg a su propia cuenta", "Error", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method if trying to send money to self
        }

        String description = subjectTextField.getText();
        try {
            User payee = masterController.getClientFromDatabase(username);
            String destinationIBAN = payee.ibans.get(0);
            accountManager.transfer(masterController.activeUser.ibans.get(0), destinationIBAN, amount, description, masterController.getDatabaseConnection());
            JOptionPane.showMessageDialog(null, "Ha enviado " + amount + "€ a " + username, "Dinero enviado", JOptionPane.INFORMATION_MESSAGE);
            masterController.transferStage.close();

        } catch (UserNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No existe ningún cliente con ese usuario. Por favor, verifique que esté escrito correctamente", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (DatabaseConnectionException e) {
            throw new RuntimeException(e);
        } catch (InsufficientBalanceException e) {
            masterController.showError("No tiene suficiente saldo");
            // JOptionPane.showMessageDialog(null, "No tiene suficiente saldo", "Saldo insuficiente", JOptionPane.WARNING_MESSAGE);
        }
    }




}