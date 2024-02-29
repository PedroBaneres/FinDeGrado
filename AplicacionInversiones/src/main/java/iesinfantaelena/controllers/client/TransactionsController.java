package iesinfantaelena.controllers.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import iesinfantaelena.controllers.MasterController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

public class TransactionsController {
    MasterController masterController;
    @FXML
    private FontAwesomeIconView backButton;

    public void setMasterController(MasterController masterController) {
        this.masterController = masterController;
        // Ahora que masterController está configurado, puedes configurar el manejador de eventos
        backButton.setOnMouseClicked(event -> {
            try {
                handleBackAction(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleBackAction(MouseEvent event) throws SQLException, IOException {
        try {
            masterController.switchToHomepage(); // Asume que este método ya existe y funciona correctamente
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Maneja la excepción como consideres necesario
        }
    }
    @FXML
    public void initialize(){

    }
}
