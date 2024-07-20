package org.example.rodeodrivediner_webapp.exceptions;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String emailOrUsernameAlreadyExists){
        super("User already exists");
    }
}
