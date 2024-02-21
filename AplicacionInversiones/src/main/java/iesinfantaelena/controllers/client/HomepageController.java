package iesinfantaelena.controllers.client;

import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class HomepageController {
    private MasterController masterController;
    @FXML
    private Label labelNombre;
    @FXML
    private Label balanceLabel;

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
        balanceLabel.setText(masterController.activeUser.getTotalBalance(masterController.getDatabaseConnection()) + "€");
        this.masterController= masterController;
    }
}