package iesinfantaelena.exceptions;

public class ServerException extends Exception{
    public ServerException(String message){
        super(message);
    }
    public ServerException(String message, Throwable t){
        super(message,t);
    }
}
