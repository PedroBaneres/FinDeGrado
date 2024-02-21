package iesinfantaelena.controllers.admin;

import iesinfantaelena.controllers.MasterController;
import iesinfantaelena.User;
import iesinfantaelena.exceptions.ClientConnectionException;
import iesinfantaelena.exceptions.MessageNotFoundException;
import iesinfantaelena.exceptions.ServerException;
import iesinfantaelena.exceptions.UserNotFoundException;
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

    private void handleClient(Socket clientSocket) {
        new Thread(() -> {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message;
                String username = in.readLine();

                User newUser = masterController.getClientFromDatabase(username);
                if(newUser == null){
                    throw new UserNotFoundException("Usuario no encontrado en la BBDD. ");
                }

                connectedClients.put(newUser, clientSocket);

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
                masterController.logSevere(e);
                masterController.showError("Error de conexión con el cliente.");
            } catch (Exception e) {
                masterController.logSevere(e);
                masterController.showError("Error inesperado en el servidor.");
            } finally {
                try {
                    if (out != null) out.close();
                    if (in != null) in.close();
                    if (clientSocket != null && !clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    masterController.logWarning("Error cerrando recursos. " + e.getMessage());
                }
            }
        }).start();
    }


    private void saveMessage(User selectedClient, String message) throws MessageNotFoundException, ClientConnectionException{
        String username = selectedClient.getUsername();
        String newMessage =  "Admin: " + message + "\n";
        String updateQuery = "UPDATE users SET conversation = CONCAT(IFNULL(conversation, ''), ?) WHERE username = ?";

        try (Connection connection = masterController.getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, newMessage);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new MessageNotFoundException("No se encontró el usuario: " + username);
            }
            System.out.println("Mensaje guardado con éxito para el usuario: " + username);

        } catch (SQLException e) {
            masterController.showError("Error al guardar el mensaje en la BBDD.");
            throw new ClientConnectionException("Error de conexión al intentar guardar el mensaje para el usuario: " + username, e);
        }
    }
    private void appendToConversation(String message) {
        conversationTextAreaAdmin.appendText(message + "\n");
    }

}
