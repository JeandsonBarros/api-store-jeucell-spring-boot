package com.apiloja.model.user;

import com.apiloja.model.ordered.Ordered;
import com.apiloja.model.products.Product;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserShopping {

	@Id
	//@Column(nullable = false, unique = true)
	private String email;


	@Column(nullable = false)
	private Long cpf;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private String birthDate;



	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String role;
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return this.role;
	}
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@ManyToMany
	private List<Product> favoriteProducts;
	
	@ManyToMany
	private List<Product> shoppingCart;

	
	@OneToMany
	private List<Ordered> ordereds;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public Long getCpf() {
		return cpf;
	}
	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Product> getFavoriteProducts() {
		return favoriteProducts;
	}
	
	public void setFavoriteProducts(Product favoriteProduct) {
		this.favoriteProducts.add(favoriteProduct);
	}
	
	public List<Product> getShoppingCart() {
		return shoppingCart;
	}
	public void setShoppingCart(Product shoppingCart) {
		this.shoppingCart.add(shoppingCart);
	}



}
