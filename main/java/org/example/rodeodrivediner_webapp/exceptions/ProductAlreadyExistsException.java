package org.example.rodeodrivediner_webapp.exceptions;

public class ProductAlreadyExistsException extends Exception{
    public ProductAlreadyExistsException(){
        super("Product already exists");
    }
}
