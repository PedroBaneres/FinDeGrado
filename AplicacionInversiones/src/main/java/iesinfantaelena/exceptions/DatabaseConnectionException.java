package iesinfantaelena.exceptions;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }
}
