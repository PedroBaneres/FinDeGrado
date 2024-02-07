package iesinfantaelena.controllers;

import iesinfantaelena.controllers.client.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class MasterController {

    private Stage stage;
    public User activeUser;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        LoginController controladorAcceso = loader.getController();
        controladorAcceso.setMasterController(this);
        controladorAcceso.setStage(stage);
        stage.show();
    }
    public void switchToRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaRegistro.fxml"));
        Parent root = loader.load();
        RegisterController registerController = loader.getController();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        registerController.init(stage2, this);
        stage2.setScene(scene);
        stage2.show();
    }
    public void switchToSupportChat() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaChat.fxml"));
        Parent root = loader.load();
        ControladorChat controladorChat = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controladorChat.initialize(stage1,this);
        stage1.show();
    }
    public boolean userExists(String usuario) {
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
            JOptionPane.showMessageDialog(null, "Error al verificar usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean verifyPassword(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.101/Bank", "admin00", "alumno");
            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(password);
            } else {
                // Username not found
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public void logIn(String username) throws IOException {
    activeUser = getClientFromDatabase(username);
    assert activeUser != null;
    if (activeUser.isAdmin()){
    logAsAdmin();} else {logAsClient();}
}
    public User getClientFromDatabase(String username) {
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.101/Bank", "admin00", "alumno");
            // Prepare SQL statement to select client information based on the username
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String mail = resultSet.getString("mail");
                String password = resultSet.getString("password");
                boolean admin = resultSet.getBoolean("admin");
                return new User(username, name, surname, mail, password, admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., log it, show an error message)
            return null;
        } return null;
    }
    private void logAsAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaChatAdmin.fxml"));
        Parent root = loader.load();
        AdminChatController controladorAdmin = loader.getController();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        controladorAdmin.initialize(this);
        stage2.show();
    }
    private void logAsClient() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaPrincipal.fxml"));
        Parent root = loader.load();
        HomepageController homepageController = loader.getController();
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        homepageController.init(stage2,this);
        stage2.setScene(scene);
        stage2.show();
    }
    public void logOut() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        LoginController controlador1 = loader.getController();
        controlador1.setMasterController(this);
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controlador1.setStage(stage1);
        stage1.show();
        activeUser = null;
    }
}
