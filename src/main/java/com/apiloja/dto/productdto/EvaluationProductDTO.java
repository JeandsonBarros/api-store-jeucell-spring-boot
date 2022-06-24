package com.apiloja.dto.productdto;


import com.apiloja.model.products.Product;
import com.apiloja.model.user.UserShopping;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


public class EvaluationProductDTO {

    private Long id;

    private String userName;

    @NotBlank
    private Long id_product;

    private String comment;

    @NotBlank
    private int quantityOfStars;

    private String date;

    public Long getId_product() {
        return id_product;
    }

    public void setId_product(Long id_product) {
        this.id_product = id_product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

