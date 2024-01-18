package com.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorAcceso {
private  Stage stage;

public boolean  isImage1 = true;
    @FXML
    private ImageView imagen;
    @FXML
    private TextField txtNombre;

    public void show() {
        this.stage.show();
    }


    @FXML
    void mostrarVentanaPrincipal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/controlador/ventanaPrincipal.fxml"));
        Parent root = loader.load();
        ControladorPrincipal controladorPrincipal = loader.getController();
        controladorPrincipal.init(txtNombre.getText(), stage, this);
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.show();
        stage.close();
    }
    @FXML
    void mostrarVentanaRegistrarse(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/controlador/ventanaRegistro.fxml"));
        Parent root = loader.load();
        ControladorRegistro controladorRegistro = loader.getController();
        controladorRegistro.init( stage, this);
        Scene scene = new Scene(root);
        Stage stage2 = new Stage();
        stage2.setScene(scene);
        stage2.show();
        stage.close();
    }


    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    void switchImage(MouseEvent event) {

        if (isImage1) {
            imagen.setImage(new Image("C:/Users/pbane/OneDrive/Escritorio/ComunicacionEntreVentanas/src/main/resources/imagenes/icono-usuario-2.jpg"));
        } else {
            imagen.setImage(new Image("C:/Users/pbane/OneDrive/Escritorio/ComunicacionEntreVentanas/src/main/resources/imagenes/icono-usuario.jpg"));
        }

        isImage1 = !isImage1;
    }
}

