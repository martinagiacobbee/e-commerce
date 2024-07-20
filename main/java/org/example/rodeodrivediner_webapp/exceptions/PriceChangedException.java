package org.example.rodeodrivediner_webapp.exceptions;

public class PriceChangedException extends Exception{

    public PriceChangedException(String prod){
        super("Price of product "+ prod+ " has changed");
    }
}
