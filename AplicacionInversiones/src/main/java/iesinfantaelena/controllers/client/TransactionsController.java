package iesinfantaelena.controllers.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;

public class TransactionsController {
    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
    }

    private void handleBackAction(ActionEvent event) {
        // Código para manejar la acción de volver atrás
    }
}
