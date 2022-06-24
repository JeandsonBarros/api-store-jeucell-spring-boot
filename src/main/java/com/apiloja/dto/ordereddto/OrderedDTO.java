package com.apiloja.dto.ordereddto;

import com.apiloja.model.products.ProductOfCart;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderedDTO {




    private Long id;

    private String date;


    private String status = "Em análise";


    private String user;


    private List<ProductOfCart> productsOredereds = new ArrayList();

    @NotNull
    private int quota;

    @NotNull
    private long cardNumber;

    @NotBlank(message = "mandatory")
    private String expirationCard;

    @NotNull
    private int cvv;

    @NotBlank(message = "mandatory")
    private String nameOnCard;

    @NotBlank(message = "mandatory")
    private String remittee;

    @NotNull
    private Long remitteeNumber;

    @NotBlank(message = "mandatory")
    private String state;

    @NotBlank(message = "mandatory")
    private String city;

    @NotBlank(message = "mandatory")
    private String district;

    @NotNull
    private Long cep;

    @NotBlank(message = "mandatory")
    private String street;

    @NotNull
    private Long numberHouse;


    private double total;

    public List<ProductOfCart> getProducts() {
        return productsOredereds;
    }

    public void setProducts(ProductOfCart products) {

        this.productsOredereds.add(products);

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationCard() {
        return expirationCard;
    }

    public void setExpirationCard(String expirationCard) {
        this.expirationCard = expirationCard;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getRemittee() {
        return remittee;
    }

    public void setRemittee(String remittee) {
        this.remittee = remittee;
    }

    public Long getRemitteeNumber() {
        return remitteeNumber;
    }

    public void setRemitteeNumber(Long remitteeNumber) {
        this.remitteeNumber = remitteeNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getCep() {
        return cep;
    }

    public void setCep(Long cep) {
        this.cep = cep;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getNumberHouse() {
        return numberHouse;
    }

    public void setNumberHouse(Long numberHouse) {
        this.numberHouse = numberHouse;
    }


}
