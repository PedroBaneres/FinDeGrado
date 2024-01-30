package iesinfantaelena.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class ControladorRegistro {
    private LoginController controladorAcceso;
    private Stage stage;

    @FXML
    private TextField txtNombreRegistro;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPasswordCheck;

    public void init(Stage stage, LoginController controlador1) {
       
        this.controladorAcceso = controlador1;
        this.stage = stage;
    }
    public void registrarse() throws IOException {

       if(comprobarError(txtCiudad) || comprobarError(txtCorreo) || comprobarError(txtUsuario) ||
        comprobarError(txtPasswordCheck) || comprobarError(txtPassword) || comprobarError(txtNombreRegistro)){
           JOptionPane.showMessageDialog(null, "Porfavor rellene todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
        } else
//comprobar si existe Usuario/correo en BBDD
        if (!txtPassword.getText().equals(txtPasswordCheck.getText())){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        else {//registrar usuario BBDD
            mostrarVentanaAcceso();
        }
    }

public boolean comprobarError(TextField textField){
    return textField.getText().isEmpty();
}
public void mostrarVentanaAcceso() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
    Parent root = loader.load();
    LoginController controlador1 = loader.getController();
    Scene scene = new Scene(root);
    Stage stage1 = new Stage();
    stage1.setScene(scene);
    controlador1.setStage(stage1);
    ((Stage)txtPassword.getScene().getWindow()).close();
    stage1.show();
}
}