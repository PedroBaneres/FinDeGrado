package iesinfantaelena.controllers.client;

import java.net.Socket;

public class Client {

    private Socket clientSocket;
    private String username;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client(String username, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.username = username;
    }
}
