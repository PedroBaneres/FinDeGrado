package com.example.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorPrincipal {
    private ControladorAcceso controlador1;
    private ControladorAjustes controladorAjustes;
    private Stage stage;
    @FXML
    private Label labelNombre;

    @FXML
    public void mostrarVentanaAcceso(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/controlador/ventanaAcceso.fxml"));
        Parent root = loader.load();
        ControladorAcceso controlador1 = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controlador1.setStage(stage1);
        Stage currentStage = (Stage)  labelNombre.getScene().getWindow();
        currentStage.close();
        stage1.show();
    }
    @FXML
    public void mostrarVentanaAjustes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/controlador/ventanaAjustes.fxml"));
        Parent root = loader.load();
        ControladorAjustes controladorAjustes = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controladorAjustes.setStage(stage1);
        Stage currentStage = (Stage)  labelNombre.getScene().getWindow();
        currentStage.close();
        stage1.show();
    }


    public void init(String text, Stage stage, ControladorAcceso controlador1) {
        labelNombre.setText("Bienvenido " + text);
        this.controlador1 = controlador1;
        this.stage = stage;
    }
/*
public class MainController {
    @FXML
    private StackPane viewContainer;

    // Your other controller logic

    // Load the views from FXML files and switch them within the StackPane
    public void switchToPrincipalView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/controlador/ventanaPrincipal.fxml"));
            Parent principalView = loader.load();
            viewContainer.getChildren().setAll(principalView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAjustesView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/controlador/ventanaAjustes.fxml"));
            Parent ajustesView = loader.load();
            viewContainer.getChildren().setAll(ajustesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Other controller methods
}
<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.example.controlador.MainController">

    <!-- Your menu buttons or items -->
    <Menu>
        <!-- Define menu items to switch between views -->
        <MenuItem onAction="#switchToPrincipalView" text="Principal"/>
        <MenuItem onAction="#switchToAjustesView" text="Ajustes"/>
        <!-- Other menu items -->
    </Menu>

    <!-- StackPane to hold different views -->
    <center>
        <StackPane fx:id="viewContainer"/>
    </center>

</BorderPane>
 */
}