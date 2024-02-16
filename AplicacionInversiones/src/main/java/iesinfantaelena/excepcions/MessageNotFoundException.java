package iesinfantaelena.excepcions;

public class MessageNotFoundException extends Exception{
    public MessageNotFoundException(String message, Throwable t){
        super(message,t);
    }
    public MessageNotFoundException(String message){
        super(message);
    }
}
