package iesinfantaelena.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

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
    private TextField txtApellido;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPasswordCheck;

    public void init(Stage stage, LoginController controlador1) {
       
        this.controladorAcceso = controlador1;
        this.stage = stage;
    }
    public void registrarse() throws IOException {

       if(comprobarError(txtCorreo) || comprobarError(txtUsuario) ||
        comprobarError(txtPasswordCheck) || comprobarError(txtPassword) || comprobarError(txtNombreRegistro) || comprobarError(txtApellido)){
           JOptionPane.showMessageDialog(null, "Porfavor rellene todos los campos", "Error", JOptionPane.WARNING_MESSAGE);
        } else
        if (!txtPassword.getText().equals(txtPasswordCheck.getText())){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
        } else
        if (usuarioExistente(txtUsuario.getText())) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya está en uso", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        } else
            // Verificar si el nombre de usuario contiene "admin"
            if (txtUsuario.getText().toLowerCase().contains("admin")) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario contiene una palabra reservada", "Error", JOptionPane.WARNING_MESSAGE);
            } else
        // Intentar registrar el usuario en la base de datos
        try {
            // Establecer la conexión con la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.101/Bank", "admin00", "alumno");
            // Crear la consulta SQL para insertar un nuevo usuario
            String sql = "INSERT INTO users (name, username, mail, surname , password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtNombreRegistro.getText());
            statement.setString(2, txtUsuario.getText());
            statement.setString(3, txtCorreo.getText());
            statement.setString(4, txtApellido.getText());
            statement.setString(5, txtPassword.getText());

            // Ejecutar la consulta
            statement.executeUpdate();

            // Cerrar la conexión
            connection.close();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            // Mostrar la ventana de acceso
            mostrarVentanaAcceso();
        } catch (SQLException | IOException e) {
            // Manejar cualquier error de SQL o IO
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para verificar si un usuario ya existe en la base de datos
    private boolean usuarioExistente(String usuario) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.101/Bank", "admin00", "alumno");
            String sql = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usuario);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            int count = resultSet.getInt("count");
            connection.close();
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al verificar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }



public boolean comprobarError(TextField textField){
        if (textField.getText()!=null) return textField.getText().isEmpty();
        else return true;
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