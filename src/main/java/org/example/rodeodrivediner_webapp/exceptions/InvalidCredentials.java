package org.example.rodeodrivediner_webapp.exceptions;

public class InvalidCredentials extends Throwable {
    public InvalidCredentials(String invalidCredentials) {
        super(invalidCredentials);
    }
}
