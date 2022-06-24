package com.apiloja.repository.products;

import com.apiloja.model.products.ProductOfCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOfCartRepository extends JpaRepository<ProductOfCart, Long> {

    List<ProductOfCart> findByUserEmail(String email);

    List<ProductOfCart> findByUserEmailAndInOrdered(String email, boolean inOrdered);
}