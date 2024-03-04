package iesinfantaelena.controllers.client;

import iesinfantaelena.User;
import iesinfantaelena.controllers.MasterController;
import iesinfantaelena.exceptions.DatabaseConnectionException;
import iesinfantaelena.exceptions.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static javax.swing.JOptionPane.showInputDialog;

public class SettingsController {
    @FXML
    private Label labelNombre;
    private  Stage stage;
    private MasterController masterController;
    @FXML
    private Label nombreTxt;
    @FXML
    private Label usuarioTxt;
    @FXML
    private Label correoTxt;

    @FXML
    private Button changeNameButton;
    @FXML
    private Button changeUsernameButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button changeEmailButton;
    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void show() {
        this.stage.show();
    }
    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        this.stage = masterController.getStage();
        updateUserName();
        if (masterController.activeUser != null) {
            updateData(masterController.activeUser);
        }
    }
    private void updateUserName() {
        if (masterController.activeUser != null) { // Asegúrate de que activeUser no es nulo
            labelNombre.setText("¿Qué deseas modificar de tu cuenta " + masterController.activeUser.getUsername() + " ?");
        }
    }
    public void goToHomepage(ActionEvent event) throws IOException, SQLException {
        masterController.switchToHomepage();
    }
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
    }
    @FXML
    public void goToSupport(ActionEvent event) throws IOException{
        masterController.showSupportChat();
    }
    @FXML
    private void handleChangeRequest(ActionEvent event) throws UserNotFoundException, DatabaseConnectionException {
        // Identifica el botón que fue presionado
        Button sourceButton = (Button) event.getSource();
        String buttonId = sourceButton.getId();
String username = masterController.activeUser.getUsername();
        // Determina qué cambiar basado en el ID del botón
        switch (buttonId) {
            case "changeNameButton":
                showInputDialog("Cambiar Nombre", "nombre");
                break;
            case "changeUsernameButton":
                showInputDialog("Cambiar usuario", "usuario");
                break;
            case "changePasswordButton":
                showInputDialog("Cambiar contraseña", "contraseña");
                break;
            case "changeEmailButton":
                showInputDialog("Cambiar Correo", "correo");
                break;
            default:
                // Maneja un caso desconocido o error
                System.out.println("Acción desconocida");
                break;
        }
        User user = masterController.getClientFromDatabase(username);
        updateData(user);
    }
    private void showInputDialog(String title, String changeType) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Por favor, introduce el nuevo " + changeType + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newValue -> updateUserData(changeType, newValue));
    }
    private void updateUserData(String changeType, String newValue) {
        // Asume que `conn` es la conexión a la base de datos obtenida previamente
        try (Connection conn = masterController.getDatabaseConnection()) {
            conn.setAutoCommit(false); // Inicia una transacción

            if ("usuario".equals(changeType)) {
                // Actualizar primero las referencias en otras tablas
                String updateAccountsSQL = "UPDATE accounts SET username = ? WHERE username = ?";
                try (PreparedStatement pstmtAccounts = conn.prepareStatement(updateAccountsSQL)) {
                    pstmtAccounts.setString(1, newValue);
                    pstmtAccounts.setString(2, masterController.activeUser.getUsername());
                    pstmtAccounts.executeUpdate();
                }
            }

            // Actualizar la tabla users
            String sql = "";
            switch (changeType) {
                case "nombre":
                    sql = "UPDATE users SET name = ? WHERE username = ?";
                    break;
                case "usuario":
                    sql = "UPDATE users SET username = ? WHERE username = ?";
                    break;
                case "contraseña":
                    sql = "UPDATE users SET password = ? WHERE username = ?";
                    break;
                case "correo":
                    sql = "UPDATE users SET mail = ? WHERE username = ?";
                    break;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newValue);
                pstmt.setString(2, masterController.activeUser.getUsername());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Actualización exitosa.");
                    conn.commit();
                } else {
                    conn.rollback();
                    System.out.println("No se pudo actualizar el usuario.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revierte las operaciones en caso de error
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateData(User user){
        nombreTxt.setText(user.getName());
        usuarioTxt.setText(user.getUsername());
        correoTxt.setText(user.getMail());
    }
}

