package com.apiloja.repository.products;

import com.apiloja.model.products.EvaluationProduct;
import com.apiloja.model.user.UserShopping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationProductRepository extends JpaRepository<EvaluationProduct, Long> {
    List<EvaluationProduct> findByProductId(Long productId);
    List<EvaluationProduct> findByUserEmail(String email);
}
