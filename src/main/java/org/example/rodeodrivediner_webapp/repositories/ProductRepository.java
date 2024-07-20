package org.example.rodeodrivediner_webapp.repositories;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByNameContainingIgnoreCase(String productName, Pageable pageable); //ignoreCase lets the query be case-insensitive
    boolean existsByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
    Optional<Product> findByProdId(int id);

    @Query("SELECT p.prodId FROM Product p WHERE p.name = :prodName")
    int getProdIdByName(@Param("prodName") String prodName);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE ?1 OR ?1 IS NULL) "
            )
    List<Product> findProductByName(String name);

    @Query("SELECT p FROM Product p  WHERE lower(p.name) LIKE lower(concat('%', :name, '%')) ORDER BY p.name ASC")
    Page<Product> findByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name, Pageable pageable);

}
