package iesinfantaelena.controllers;

import iesinfantaelena.controllers.client.SettingsController;
import iesinfantaelena.controllers.admin.AdminChatController;
import iesinfantaelena.controllers.client.HomepageController;
import iesinfantaelena.User;
import iesinfantaelena.controllers.client.SupportChatController;
import iesinfantaelena.excepcions.ServerException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterController {

    private static final Logger LOGGER = Logger.getLogger(MasterController.class.getName());
    private Stage stage;
    private Stage chatStage;
    private Socket connectionSocket;

    private ServerSocket serverSocket;
    public User activeUser;
    public Stage getStage() {
        return stage;
    }

    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        LoginController controladorAcceso = loader.getController();
        controladorAcceso.setMasterController(this);
        this.stage = stage;
        stage.show();
    }
    public void switchToRegistration() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaRegistro.fxml"));
        Parent root = loader.load();
        RegisterController registerController = loader.getController();
        Scene scene = new Scene(root);
        registerController.initialize( this);
        stage.setScene(scene);
    }
    public void switchToSupportChat() throws IOException{
        if (chatStage==null){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaChat.fxml"));
        Parent root = loader.load();
        SupportChatController supportChatController = loader.getController();
        Scene scene = new Scene(root);
        chatStage = new Stage();
        chatStage.setScene(scene);
        supportChatController.initialize(chatStage,this);
        connectionSocket = supportChatController.getSocket();}
        else chatStage.hide();
        chatStage.show();
    }
    public void switchToSettings() throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAjustes.fxml"));
            Parent root = loader.load();
            SettingsController settingsController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            settingsController.initialize(this);
        }
    public void switchToHomepage() throws IOException{
        logAsClient();
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
    public void logIn(String username) throws IOException, ServerException {
    activeUser = getClientFromDatabase(username);
    assert activeUser != null;
    if (activeUser.isAdmin()){
    logAsAdmin();} else {logAsClient();}
}
    public User getClientFromDatabase(String username) {
        try {
            // Establish connection to the database
            Connection connection = getDatabaseConnection();
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
    private void logAsAdmin() throws IOException, ServerException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaChatAdmin.fxml"));
        Parent root = loader.load();
        AdminChatController controladorAdmin = loader.getController();
        Scene scene = new Scene(root);
        controladorAdmin.initialize(this);
        stage.setScene(scene);
    }
    private void logAsClient() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaPrincipal.fxml"));
        Parent root = loader.load();
        HomepageController homepageController = loader.getController();
        Scene scene = new Scene(root);
        homepageController.initialize(this);
        stage.setScene(scene);
    }
    public void logOut() throws IOException {
        //closeServer();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        LoginController controlador = loader.getController();
        controlador.setMasterController(this);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        if (chatStage!=null)chatStage.close();
        activeUser = null;
    }
    public Connection getDatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://192.168.56.101/Bank";
        String username = "admin00";
        String password = "alumno";

        return DriverManager.getConnection(url, username, password);
    }
    public void logSevere(Exception e) {
        //error GRAVE
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }

    public void logWarning(String message) {
        // error o advertencia
        LOGGER.log(Level.WARNING, message);
    }
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
    public String loadConversation(User user){
        String conversation = "";
        String username = user.getUsername();
        try (Connection connection = getDatabaseConnection()) {
            String query = "SELECT conversation FROM users WHERE username = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        conversation = resultSet.getString("conversation");
                    } else {
                        System.out.println("No conversation found for user: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversation;
    }
    public void closeServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logSevere(e);
            }
        }
    }
}
