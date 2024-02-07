package iesinfantaelena.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class RegisterController {
    private Stage stage;
    private  MasterController masterController;
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

    public void init(Stage stage, MasterController masterController) {
        this.masterController = masterController;
        this.stage = stage;
    }
    public void register() throws IOException {

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
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.101/Bank", "admin00", "alumno");
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
            stage.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean comprobarError(TextField textField){
        if (textField.getText()!=null) return textField.getText().isEmpty();
        else return true;
}
}