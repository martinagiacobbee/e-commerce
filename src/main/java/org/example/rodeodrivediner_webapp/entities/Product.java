package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="prod_id", nullable = false)
    private int prodId;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="description")
    private String description;

    @NotNull
    @Column(name="price")
    private float price;


    @Column(name="quantity")
    private int quantity;

    @Version
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private long version;

    @Column(name="url", nullable = false)
    private String url;

    @OneToMany(targetEntity = ProductInCart.class, mappedBy="product",cascade= CascadeType.MERGE)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductInCart> productsInCart;

}
