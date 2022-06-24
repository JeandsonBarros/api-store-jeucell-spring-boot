package com.apiloja.model.products;

import com.apiloja.model.user.UserShopping;

import javax.persistence.*;

@Entity
public class EvaluationProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private UserShopping user;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantityOfStars;

    @Column(nullable = false)
    private String date;

    private String comment;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserShopping getUser() {
        return user;
    }

    public void setUser(UserShopping user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getQuantityOfStars() {
        return quantityOfStars;
    }

    public void setQuantityOfStars(int quantityOfStars) {
        this.quantityOfStars = quantityOfStars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
