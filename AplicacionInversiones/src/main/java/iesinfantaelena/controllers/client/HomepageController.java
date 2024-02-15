package iesinfantaelena.controllers.client;

import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {
    private MasterController masterController;
    @FXML
    private Label labelNombre;

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

    public void initialize(MasterController masterController) {
        labelNombre.setText("Bienvenido " + masterController.activeUser.getUsername());
        this.masterController= masterController;
    }
}