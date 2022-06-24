package com.apiloja.dto.productdto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProductDTO {


    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull
    private double price;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotNull
    private Long quantity;

    @NotBlank(message = "ImgUrl is mandatory")
    private String imgUrl;

    @NotBlank(message = "ImgName is mandatory")
    private String imgName;

    private Long starsQuantity;

    private Long evaluationQuantity;

    public Long getStarsQuantity() {
        return starsQuantity;
    }

    public void setStarsQuantity(Long starsQuantity) {
        this.starsQuantity = starsQuantity;
    }

    public Long getEvaluationQuantity() {
        return evaluationQuantity;
    }

    public void setEvaluationQuantity(Long evaluationQuantity) {
        this.evaluationQuantity = evaluationQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
