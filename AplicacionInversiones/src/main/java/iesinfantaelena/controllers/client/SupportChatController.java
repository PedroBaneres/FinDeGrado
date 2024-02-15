package iesinfantaelena.controllers.client;

import iesinfantaelena.controllers.MasterController;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportChatController {
    private MasterController masterController;
    @FXML
    private Label labelNombre;
    @FXML
    private TextArea conversationTextArea;

    @FXML
    private TextField messageTextField;



    private Socket socket;
    public Socket getSocket() {
        return socket;
    }
    private PrintWriter out;
    private BufferedReader in;

    public void initialize(Stage stage, MasterController masterController) {
        this.masterController = masterController;
        try {
            socket = new Socket("localhost", 6000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(masterController.activeUser.getUsername());
            Thread serverInputHandler = new Thread(() -> {
                try {
                    conversationTextArea.setText(masterController.loadConversation(masterController.activeUser));
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
        saveMessage(message);
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

    public void saveMessage(String message){
   String username = masterController.activeUser.getUsername();
   String newMessage = username + ": " + message + "\n";
    try (Connection connection = masterController.getDatabaseConnection()) {
        String updateQuery = "UPDATE users SET conversation = CONCAT(IFNULL(conversation, ''), ?) WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newMessage);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Message saved successfully.");
            } else {
                System.out.println("No rows affected. User not found or text column is null.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle database errors appropriately
    }
}
    }
