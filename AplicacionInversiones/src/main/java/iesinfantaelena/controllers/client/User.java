package iesinfantaelena.controllers.client;

import java.net.Socket;

public class User {

    private Socket clientSocket;
    private String username;

    private String name;
    private String surname;
    private String mail;

    private String password;
    private boolean admin;

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

    public User(String username, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.username = username;
    }

    public User(String username, String name, String surname, String mail, String password,boolean admin) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.admin=admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
