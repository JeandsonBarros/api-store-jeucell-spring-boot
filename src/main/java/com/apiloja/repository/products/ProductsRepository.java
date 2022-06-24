package com.apiloja.repository.products;

import com.apiloja.model.products.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long>{


    Page<Product> findByNameOrCategoryOrDescriptionContaining(String name, String category, String description, Pageable page);
    Page<Product> findByCategory(String category, Pageable paging);


}
