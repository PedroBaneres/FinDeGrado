package iesinfantaelena.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message, Throwable t){
        super(message, t);
    }
    public UserNotFoundException(String message){
        super(message);
    }
}
