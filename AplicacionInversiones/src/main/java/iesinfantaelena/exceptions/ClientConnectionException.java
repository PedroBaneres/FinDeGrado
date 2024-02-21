package iesinfantaelena.exceptions;

public class ClientConnectionException extends Exception{
    public ClientConnectionException(String message, Throwable t){
        super (message, t);
    }
    public ClientConnectionException(String message){
        super(message);
    }
}
