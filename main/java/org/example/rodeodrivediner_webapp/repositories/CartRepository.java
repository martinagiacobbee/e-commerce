package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByCust(Customer customer);

    @Query("select c from Cart c where c.purchaseTime > ?1 and c.purchaseTime < ?2 and c.cust = ?3")
    List<Cart> findByCustomerInPeriod(Date startDate, Date endDate, Customer cust);

    @EntityGraph(attributePaths = {"productsInCart"})
    Cart findByCartId (long id);

    boolean existsByCust(Customer customer);
}
