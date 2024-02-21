package iesinfantaelena;

import java.io.Serializable;
import java.net.Socket;
import java.util.regex.Pattern;

public class User implements Serializable {
    private Socket clientSocket;
    private String username;

    private String name;
    private String surname;
    private String mail;
    private String password;
    private boolean admin;

    public User(String username, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.username = username;
    }

    public User(String username, String name, String surname, String mail, String password,boolean admin) {
        setUsername(username);
        setName(name);
        setSurname(surname);
        setMail(mail);
        setPassword(password);
        setAdmin(admin);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        if (clientSocket == null) {
            throw new IllegalArgumentException("El socket del cliente no puede ser nulo");
        }
        this.clientSocket = clientSocket;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        this.username = username;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.surname = surname;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        if (mail == null ) {
            throw new IllegalArgumentException("El correo electrónico no es válido");
        }
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("La contraseña no es válida");
        }
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
