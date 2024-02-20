package iesinfantaelena.controllers;

import iesinfantaelena.excepcions.DatabaseConnectionException;
import iesinfantaelena.excepcions.ServerException;
import iesinfantaelena.excepcions.UserNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.IOException;

public class LoginController {
    private MasterController masterController;
    public boolean isImage1 = true;
    @FXML
    private ImageView imagen;
    @FXML
    private TextField txtNombre;
    @FXML
    private PasswordField txtPassword;

    public void setMasterController(MasterController masterController) {
        this.masterController = masterController;
    }
    @FXML
    void logIn(ActionEvent event) throws IOException, ServerException, UserNotFoundException, DatabaseConnectionException {
        String username = txtNombre.getText();
        if (!masterController.userExists(username)) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario no existe. Por favor, verifique que esté escrito correctamente o regístrese si aún no dispone de una cuenta.", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            String password = txtPassword.getText();
            if (masterController.verifyPassword(username, password)) {
                masterController.logIn(username);
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Por favor, inténtelo de nuevo.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }

}
    @FXML
    void switchToRegistration(ActionEvent event) throws IOException {
        masterController.switchToRegistration();
    }
    @FXML
    void switchImage(MouseEvent event) {
        if (isImage1) {
            imagen.setImage(new Image(getClass().getResource("/images/icono-usuario.jpg").toExternalForm()));
        } else {
            imagen.setImage(new Image(getClass().getResource("/images/icono-usuario-2.jpg").toExternalForm()));
        }

        isImage1 = !isImage1;
    }

}

