package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name="products_in_cart")
public class ProductInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int prodInCartId;

    @ManyToOne
    @JoinColumn(name = "cart")
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

    @Basic
    @Column(name = "quantity", nullable = true)
    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product")
    private Product product;

    @Basic
    @Column(name = "price")
    private float price;


    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof ProductInCart)) return false;
        ProductInCart c = (ProductInCart) o;
        return c.prodInCartId==prodInCartId;
    }
}
