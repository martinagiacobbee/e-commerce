package org.example.rodeodrivediner_webapp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="cart")
@Data
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id",nullable = false)
    private Integer cartId;

    @ManyToOne
    @JoinColumn(name = "cust")
    private Customer cust;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date purchaseTime;

    @OneToMany(mappedBy="cart",cascade=CascadeType.MERGE)
    private List<ProductInCart> productsInCart=new ArrayList<>();

}
