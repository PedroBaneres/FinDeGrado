package iesinfantaelena.controllers.client;

import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    @FXML
    private Label labelNombre;
    private  Stage stage;
    private MasterController masterController;
    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void show() {
        this.stage.show();
    }
    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        this.stage = masterController.getStage();
    }
    public void goToHomepage(ActionEvent event) throws IOException {
        masterController.switchToHomepage();
    }
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
    }
    @FXML
    public void goToSupport(ActionEvent event) throws IOException{
        masterController.switchToSupportChat();
    }
    }

