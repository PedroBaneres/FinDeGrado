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
    public void mostrarVentanaAcceso(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ventanaAcceso.fxml"));
        Parent root = loader.load();
        LoginController controlador1 = loader.getController();
        Scene scene = new Scene(root);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        controlador1.setStage(stage1);
        Stage currentStage = (Stage) labelNombre.getScene().getWindow();
        currentStage.close();

            serverSocket.close();

        stage1.show();
    }

    public void initialize(MasterController masterController) {
        this.masterController = masterController;
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
                int i = 0;
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    User user = new User("Cliente " + i, clientSocket); //aqui sencillamente se cogeria el cliente de la bdd y se le asigna el socket como cliente
                    handleClient(user);
                    i++;
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
                    appendToConversation(user.getUsername()  +": " + message);
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
