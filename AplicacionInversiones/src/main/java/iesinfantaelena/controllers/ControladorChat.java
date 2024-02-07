package iesinfantaelena.controllers;

import iesinfantaelena.controllers.client.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class    ControladorChat {
    private MasterController masterController;
    private  Stage stage;
    @FXML
    private Label labelNombre;
    @FXML
    private TextArea conversationTextArea;

    @FXML
    private TextField messageTextField;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void show() {
        this.stage.show();
    }
    @FXML
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
        stage.close();
    }

    @FXML
    void goToHomepage(ActionEvent event) throws IOException {
        User user = masterController.activeUser;

    }
    @FXML
    public void mostrarVentanaAjustes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAjustes.fxml"));
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
    public void initialize(Stage stage, MasterController masterController) {
        this.stage = stage;
        this.masterController = masterController;
        try {
            socket = new Socket("localhost", 6000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread serverInputHandler = new Thread(() -> {
                try {
                    String serverInput;
                    while ((serverInput = in.readLine()) != null) {
                        appendToConversation("Soporte: " + serverInput);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverInputHandler.start();
        } catch (IOException e) {
            conversationTextArea.setText("Soporte fuera de servicio"); //añadir boton refresh que se muestre
        }
    }

    @FXML
    public void sendMessage() {
        String message = messageTextField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            appendToConversation(masterController.activeUser.getUsername() + ": " + message);
            messageTextField.clear();
        }
    }
    private void appendToConversation(String message) {
        conversationTextArea.appendText(message + "\n");
    }
}
