package org.example.rodeodrivediner_webapp.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("User not found");
    }

}

