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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class AdminChatController {

    private Stage stage;
    @FXML
    private Label labelNombre;
    @FXML
    private TextArea conversationTextAreaAdmin;
    @FXML
    private TextField messageTextFieldAdmin;
    private MasterController masterController;
    private ServerSocket serverSocket;
    private PrintWriter out;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        this.stage.show();
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
        masterController.logOut();
    }

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
        this.stage = masterController.getStage();
        try {
            serverSocket = new ServerSocket(6000);
            startServer();
        } catch (IOException e) {
            conversationTextAreaAdmin.setPromptText("Servidor iniciado");
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    masterController.activeUser.setClientSocket(clientSocket); //aqui sencillamente se cogeria el cliente de la bdd y se le asigna el socket como cliente
                    handleClient(masterController.activeUser);

                }
            } catch (SocketException e) {
            //fantasma
        } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(User user) {
        new Thread(() -> {
            try {
                out = new PrintWriter(user.getClientSocket().getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(user.getClientSocket().getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    // Parse client's username from the message
                    String[] parts = message.split(": ", 2);
                    if (parts.length == 2) {
                        String clientUsername = parts[0];
                        String clientMessage = parts[1];
                        appendToConversation(clientUsername + ": " + clientMessage);
                    } else {
                        // If unable to parse username, just display the message
                        appendToConversation(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }




    @FXML
    public void sendMessage() {
        String message = messageTextFieldAdmin.getText();
        if (!message.isEmpty()) {
            out.println(message);
            appendToConversation("Admin: " + message);
            messageTextFieldAdmin.clear();
        }
    }

    private void appendToConversation(String message) {
        conversationTextAreaAdmin.appendText(message + "\n");
    }


}
