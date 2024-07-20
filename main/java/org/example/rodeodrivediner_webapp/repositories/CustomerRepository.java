package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByFirstNameAndLastName(String firstName, String lastName);
    Customer findByUsernameIgnoreCase(String username);
    List<Customer> findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);
    List<Customer> findByEmail(String email);
    boolean existsByUsername(String username);
    Customer findByCustId(int id);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Customer u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) ORDER BY u.username")
    List<Customer> findByUsernameContainingOrderByUsernameIgnoreCase(String username);
    List<Customer> findByFirstNameContainingIgnoreCase(String firstname);
    List<Customer> findByLastNameContainingIgnoreCase(String lastname);

    boolean existsByUsernameIgnoreCase(String username);
}

