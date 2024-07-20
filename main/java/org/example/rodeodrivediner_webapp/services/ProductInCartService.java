package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.example.rodeodrivediner_webapp.repositories.ProductInCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductInCartService {
    /*@Autowired
    private ProductInCartRepository productInCartRepository;

    @Transactional(readOnly = false)
    public void addProduct(Product prodotto){
        ProductInCart carrelloClothing = productInCartRepository.findByProducts(prodotto);
        carrelloClothing.setQuantity(carrelloClothing.getQuantity()+1);
        productInCartRepository.save(carrelloClothing);
    }

    @Transactional(readOnly = false)
    public void removeProduct(Product prodotto) {
        ProductInCart carrelloClothing = productInCartRepository.findByProducts(prodotto);
        carrelloClothing.setQuantity(carrelloClothing.getQuantity() - 1);
        if (carrelloClothing.getQuantity() == 0)
            productInCartRepository.delete(carrelloClothing);
        else
            productInCartRepository.save(carrelloClothing);
    }*/
}
