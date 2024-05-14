package iesinfantaelena.controllers.client;

import iesinfantaelena.User;
import iesinfantaelena.controllers.MasterController;
import iesinfantaelena.exceptions.DatabaseConnectionException;
import iesinfantaelena.exceptions.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
            labelNombre.setText("Revisé su configuración " + masterController.activeUser.getUsername());
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
   
        Button sourceButton = (Button) event.getSource();
        String buttonId = sourceButton.getId();
        String username = masterController.activeUser.getUsername();
        // Determina qué cambiar basado en el ID del botón
        switch (buttonId) {
            case "changeNameButton":
                showInputDialog("Cambiar Nombre", "nombre");
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
        if ("contraseña".equals(changeType)) {
            // Crea un diálogo personalizado para la contraseña
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle(title);
            dialog.setHeaderText(null);

            // Configura el tipo de botones en el diálogo
            ButtonType okButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            // Crea un PasswordField para ingresar la contraseña
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Contraseña");

            // Añade el PasswordField al diálogo
            dialog.getDialogPane().setContent(passwordField);

            // Convierte el resultado en un string cuando el botón Aceptar es presionado
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return passwordField.getText();
                }
                return null;
            });

            // Muestra el diálogo y captura el resultado
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newValue -> updateUserData(changeType, newValue));
        } else {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle(title);
            dialog.setHeaderText(null);
            dialog.setContentText("Por favor, introduce el nuevo " + changeType + ":");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newValue -> updateUserData(changeType, newValue));
        }
    }
    private void updateUserData(String changeType, String newValue) {
        // Asume que `conn` es la conexión a la base de datos obtenida previamente
        try (Connection conn = masterController.getDatabaseConnection()) {
            conn.setAutoCommit(false); // Inicia una transacción

            boolean esValido;
            switch (changeType) {
                case "nombre":
                    esValido = esNombreValido(newValue);
                    break;
                case "contraseña":
                    esValido = esContrasenaValida(newValue);
                    break;
                case "correo":
                    esValido = esCorreoValido(newValue);
                    break;
                default:
                    esValido = false;
                    break;
            }

            if (!esValido) {
                masterController.showError("El ajuste introducido no es válido por favor pruebe de nuevo.");
                return;
            }


            String sql = "";
            switch (changeType) {
                case "nombre":
                    sql = "UPDATE users SET name = ? WHERE username = ?";
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
        correoTxt.setText(user.getMail());
    }
    private boolean esNombreValido(String nombre) {
        String regex = "^[A-Za-z ]+$";
        return nombre.matches(regex);
    }
    private boolean esContrasenaValida(String contrasena) {
        return contrasena.length() >= 8;
    }
    private boolean esCorreoValido(String correo) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return correo.matches(regex);
    }
}

