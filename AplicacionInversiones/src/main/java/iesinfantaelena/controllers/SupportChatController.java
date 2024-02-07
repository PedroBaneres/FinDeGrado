package iesinfantaelena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SupportChatController {
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
    }

    @FXML
    public void goToHomepage(ActionEvent event) throws IOException {
        masterController.switchToHomepage();
    }

    @FXML
    public void goToSettings(ActionEvent event) throws IOException{
        masterController.switchToSettings();
        stage.close();
    }

    public void initialize(Stage stage, MasterController masterController) {

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
            String username = masterController.activeUser.getUsername();
            out.println(username + ": " + message); // Send username along with the message
            appendToConversation(username + ": " + message);
            messageTextField.clear();
        }
    }

    private void appendToConversation(String message) {
        conversationTextArea.appendText(message + "\n");
    }
}
