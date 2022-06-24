package com.apiloja.model.products;

import com.apiloja.model.products.Product;
import com.apiloja.model.user.UserShopping;

import javax.persistence.*;

@Entity
public class ProductOfCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private UserShopping user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private boolean inOrdered;

    public Product getProduct() {
        return product;
    }

    public UserShopping getUser() {
        return user;
    }


    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUser(UserShopping user) {
        this.user = user;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInOrdered() {
        return inOrdered;
    }

    public void setInOrdered(boolean inOrdered) {
        this.inOrdered = inOrdered;
    }
}
