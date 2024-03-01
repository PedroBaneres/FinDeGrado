package iesinfantaelena;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    public List<String> ibans;
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
    public double getTotalBalance(Connection connection) throws SQLException {
            double totalBalance = 0.0;
            String query = "SELECT balance FROM Bank.accounts WHERE username = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    totalBalance += resultSet.getDouble("balance");
                }
            }

        return totalBalance;
        }


        public void setIBANs(Connection connection) throws SQLException {
        List<String> ibans = new ArrayList<>();
        String sql = "SELECT IBAN FROM accounts WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);


            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    String iban = resultSet.getString("IBAN");

                    ibans.add(iban);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        this.ibans = ibans;
    }

}

