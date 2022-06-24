package com.apiloja.model.ordered;

import com.apiloja.model.products.ProductOfCart;
import com.apiloja.model.user.UserShopping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ordered {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String date;

	@Column(nullable = false)
	private String status = "Em análise";

	@JoinColumn(nullable = false)
	@ManyToOne
	private UserShopping user;

	@JoinColumn(nullable = false)
	@ManyToMany
	private List<ProductOfCart> productsOredereds = new ArrayList();

	@Column(nullable = false)
	private int quota;

	@Column(nullable = false)
	private long cardNumber;

	@Column(nullable = false)
	private String expirationCard;

	@Column(nullable = false)
	private int cvv;

	@Column(nullable = false)
	private String nameOnCard;

	@Column(nullable = false)
	private String remittee;

	@Column(nullable = false)
	private Long remitteeNumber;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String district;

	@Column(nullable = false)
	private Long cep;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private Long numberHouse;

	@Column(nullable = false)
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserShopping getUser() {
		return user;
	}

	public void setUser(UserShopping user) {
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
