package iesinfantaelena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {
    private MasterController masterController;
    private ControladorAjustes controladorAjustes;
    private Stage stage;
    @FXML
    private Label labelNombre;

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
        stage.close();
    }
    @FXML
    public void mostrarVentanaAjustes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAjustes.fxml"));
        Parent root = loader.load();
        ControladorAjustes controladorAjustes = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controladorAjustes.setStage(stage1);
        Stage currentStage = (Stage)  labelNombre.getScene().getWindow();
        currentStage.close();
        stage1.show();
    }
    @FXML
    public void switchToSupport(ActionEvent event) throws IOException{
        masterController.switchToSupportChat();
        stage.close();
    }

    public void init(Stage stage, MasterController masterController) {
        labelNombre.setText("Bienvenido " + masterController.activeUser.getUsername());
        this.masterController= masterController;
        this.stage = stage;
    }
}