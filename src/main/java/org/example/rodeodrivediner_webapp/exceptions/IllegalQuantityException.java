package org.example.rodeodrivediner_webapp.exceptions;

import org.example.rodeodrivediner_webapp.entities.Product;

public class IllegalQuantityException extends Exception{
    public IllegalQuantityException(){
        super("Invalid Quantity");
    }

    public IllegalQuantityException(Product prod){
        System.out.println("Invalid Quantity in product: "+ prod.getName() + "having id: "+ prod.getProdId());
    }


}
