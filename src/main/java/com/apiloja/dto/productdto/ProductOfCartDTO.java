package com.apiloja.dto.productdto;

import com.apiloja.model.products.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductOfCartDTO {

    private Long id;

    private String user;

    @NotBlank(message = "mandatory")
    private Product product;

    @NotNull
    private Long quantity;

    private double total;

    private boolean inOrdered;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isInOrdered() {
        return inOrdered;
    }

    public void setInOrdered(boolean inOrdered) {
        this.inOrdered = inOrdered;
    }
}
