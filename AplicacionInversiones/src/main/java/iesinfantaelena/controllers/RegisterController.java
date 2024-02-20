package iesinfantaelena.controllers;

import iesinfantaelena.excepcions.DatabaseConnectionException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class RegisterController {
    private Stage stage;
    private MasterController masterController;
    @FXML
    private TextField txtNombreRegistro;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtApellido;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordCheck;

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        this.stage = masterController.getStage();
    }
    public void register() throws IOException, DatabaseConnectionException {

        if(comprobarError(txtCorreo) || comprobarError(txtUsuario) ||
        comprobarError(txtPasswordCheck) || comprobarError(txtPassword) || comprobarError(txtNombreRegistro) || comprobarError(txtApellido)){
           JOptionPane.showMessageDialog(null, "Porfavor rellene todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
        } else if (!txtPassword.getText().equals(txtPasswordCheck.getText())){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else if (masterController.userExists(txtUsuario.getText())) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya está en uso", "Error", JOptionPane.WARNING_MESSAGE);
        } else if (txtUsuario.getText().toLowerCase().contains("admin")) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario contiene una palabra reservada", "Error", JOptionPane.WARNING_MESSAGE);
            } else
        try {
            Connection connection = masterController.getDatabaseConnection();
            String sql = "INSERT INTO users (name, username, mail, surname , password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtNombreRegistro.getText());
            statement.setString(2, txtUsuario.getText());
            statement.setString(3, txtCorreo.getText());
            statement.setString(4, txtApellido.getText());
            statement.setString(5, txtPassword.getText());

            statement.executeUpdate();
            connection.close();

            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            masterController.logOut();
        } catch (SQLException | IOException e) {
            masterController.showError("Error al registrar el usuario. Compruebe que los campos han sido rellenados correctamente");
            throw new DatabaseConnectionException("Error al conectar con la BBDD.");
        }
    }
    public boolean comprobarError(TextField textField){
        if (textField.getText()!=null) return textField.getText().isEmpty();
        else return true;
    }
}