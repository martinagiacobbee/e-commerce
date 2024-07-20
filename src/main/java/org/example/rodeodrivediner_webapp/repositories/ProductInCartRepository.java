package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductInCartRepository extends JpaRepository<ProductInCart, Integer> {

    List<ProductInCart> findByProduct(Product prodotto);
    boolean existsByProduct(Product prodotto);
    boolean existsByCartAndProduct(Cart carrello, Product prodotto);
    ProductInCart findByCartAndProduct(Cart carrello, Product prodotto);

}
