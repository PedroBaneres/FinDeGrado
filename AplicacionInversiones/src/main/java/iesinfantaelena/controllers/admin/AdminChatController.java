package iesinfantaelena.controllers.admin;

import iesinfantaelena.controllers.MasterController;
import iesinfantaelena.User;
import iesinfantaelena.excepcions.ClientConnectionException;
import iesinfantaelena.excepcions.MessageNotFoundException;
import iesinfantaelena.excepcions.ServerException;
import iesinfantaelena.excepcions.UserNotFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

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
    private HashMap<User, Socket> connectedClients = new HashMap<>();
    private PrintWriter out;
    private User selectedClient;
    private PrintWriter selectedClientOut;
    @FXML
    private ChoiceBox<String> connectedClientsChoiceBox;

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

    public void initialize(MasterController masterController) throws ServerException {
        this.masterController = masterController;
        this.stage = masterController.getStage();

        try {
            conversationTextAreaAdmin.setPromptText("Servidor Iniciado");

           if (serverSocket==null || serverSocket.isClosed()) {
               serverSocket = new ServerSocket(6000);
               startServer();
           }

        } catch (IOException e) {
            masterController.logSevere(e);
            conversationTextAreaAdmin.setPromptText("Error al iniciar servidor");
            throw new ServerException("Ha ocurrido un error al conectar con el servidor en el puerto 6000.", e.getCause());
        }
    }
    @FXML
    private void selectClient(ActionEvent event) throws ClientConnectionException , UserNotFoundException{
        String username = connectedClientsChoiceBox.getValue();

        if (username != null) {
            String oldConversation = ""; // asegurar de que este inicializada la conversacion

            for (User user : connectedClients.keySet()) {
                if (username.equals(user.getUsername())){
                    selectedClient = user;
                    Socket clientSocket = connectedClients.get(user);

                    try {
                        selectedClientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                        oldConversation = masterController.loadConversation(user);

                    } catch (IOException e) {
                        masterController.logSevere(e);
                        throw new ClientConnectionException("Error al conectar con el cliente: " + e.getMessage(), e);
                    }
                }
            }
            conversationTextAreaAdmin.setText(oldConversation);
        } else {
            masterController.logWarning("No se seleccionó ningún usuario");
            throw new UserNotFoundException("El usuario no ha sido seleccionado.");
        }
    }

    @FXML
    public void sendMessage() {
        String message = messageTextFieldAdmin.getText();
        if (!message.isEmpty() && selectedClientOut != null) {

            try {
                saveMessage(selectedClient, message);
                selectedClientOut.println(message);
                appendToConversation("Admin: " + message);
                messageTextFieldAdmin.clear();

            } catch (MessageNotFoundException e){
                masterController.showError("Error al guardar el mensaje: " + e.getMessage());
                masterController.logWarning("Mensaje no encontrado: " + e.getMessage());

            } catch (ClientConnectionException e){
                masterController.showError("Error al conectar con el cliente: " + e.getMessage());
                masterController.logSevere(e);
            }
        } else {
            masterController.showError("No se puede enviar un mensaje vacío o la conexión con el cliente no está establecida.");
        }
    }
    private void startServer() {
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (SocketException e) {
                masterController.logWarning("Servidor cerrado o error de conexión. " + e.getMessage());
            } catch (IOException e) {
                masterController.logSevere(e);
            } finally {
                masterController.closeServer();
            }
        }).start();
    }

    private void handleClient(Socket clientsocket) {
        new Thread(() -> {
            try {
                out = new PrintWriter(clientsocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                String message;
                String username = in.readLine();
                User newUser = masterController.getClientFromDatabase(username);
                connectedClients.put(newUser, clientsocket);

                Platform.runLater(() -> {
                    String choice = connectedClientsChoiceBox.getValue();
                    ObservableList<String> connectedClientsList = FXCollections.observableArrayList();
                    for (User user : connectedClients.keySet()) {
                        connectedClientsList.add(user.getUsername());
                    }
                    connectedClientsChoiceBox.setItems(connectedClientsList);
                    connectedClientsChoiceBox.setValue(choice);
                });
                while ((message = in.readLine()) != null) {
                    String[] parts = message.split(": ", 2);
                    if (parts.length == 2 ) {
                        String clientUsername = parts[0];
                        String clientMessage = parts[1];
                        if(connectedClientsChoiceBox.getValue().equals(clientUsername))
                            appendToConversation(clientUsername + ": " + clientMessage);
                    } else {
                        appendToConversation(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void saveMessage(User selectedClient, String message) throws MessageNotFoundException, ClientConnectionException{
        String username = selectedClient.getUsername();
        String newMessage =  "Admin: " + message + "\n";
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

    private void appendToConversation(String message) {
        conversationTextAreaAdmin.appendText(message + "\n");
    }

}
