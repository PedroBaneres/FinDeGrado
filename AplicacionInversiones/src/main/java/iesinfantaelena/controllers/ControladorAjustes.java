package iesinfantaelena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorAjustes {
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
    @FXML
    void mostrarVentanaPrincipal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaPrincipal.fxml"));
        Parent root = loader.load();
        HomepageController homepageController = loader.getController();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.show();
        stage.close();
    }
    @FXML
    public void mostrarVentanaAcceso(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        LoginController controlador1 = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controlador1.setStage(stage1);
        Stage currentStage = (Stage)  labelNombre.getScene().getWindow();
        currentStage.close();
        stage1.show();
    }
    @FXML
    public void mostrarVentanaChat(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaChat.fxml"));
            Parent root = loader.load();
            ControladorChat controladorChat = loader.getController();
            Scene scene = new Scene(root);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            controladorChat.setStage(stage1);
            Stage currentStage = (Stage)  labelNombre.getScene().getWindow();
            currentStage.close();
            controladorChat.initialize(stage1,masterController);
            stage1.show();
        }
    }

