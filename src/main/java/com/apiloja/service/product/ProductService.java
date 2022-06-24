package com.apiloja.service.product;

import com.apiloja.dto.productdto.EvaluationProductDTO;
import com.apiloja.dto.productdto.ProductDTO;
import com.apiloja.model.products.EvaluationProduct;
import com.apiloja.model.products.Product;
import com.apiloja.model.user.UserShopping;
import com.apiloja.repository.products.EvaluationProductRepository;
import com.apiloja.repository.products.ProductsRepository;
import com.apiloja.repository.user.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProductService {
    ProductsRepository pR;
    EvaluationProductRepository ePR;
    UsersRepository uR;

    public ProductService(UsersRepository uR, ProductsRepository pR, EvaluationProductRepository ePR) {
        this.pR = pR;
        this.ePR = ePR;
        this.uR = uR;
    }

    public Page<ProductDTO>getAll(Pageable paging) {

        List<ProductDTO> productDTOS = new ArrayList<>();

        pR.findAll(paging).forEach(data ->{
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(data, productDTO);
            productDTOS.add(productDTO);
        });

        return new PageImpl<>(productDTOS, paging, pR.findAll(paging).getTotalElements());

    }

    public Map<String, List<Product>> getHome() {

        String[] categorys = {"Aparelhos-de-som",
                "Smartphones",
                "Acessórios-para-smartphones",
                "Perifericos-para-computador"};

        Pageable paging = PageRequest.of(0, 5, Sort.by("status"));

        Map<String, List<Product>> home = new HashMap<>();

        for(String category : categorys) {
            home.put(category, pR.findByCategory(category, paging).getContent());
        }

        return home;
    }

    public List<ProductDTO> faindFast(String nameProduct){
        Pageable page = PageRequest.of(0, 4, Sort.by("name"));

        List<ProductDTO> productDTOS = new ArrayList<>();

        pR.findByNameOrCategoryOrDescriptionContaining(nameProduct, nameProduct, nameProduct, page).forEach(data ->{
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(data, productDTO);
            productDTOS.add(productDTO);
        });
        return productDTOS;

    }

    public Page<ProductDTO> getProductForCategory(Pageable page, String category) {

        List<ProductDTO> productDTOS = new ArrayList<>();
        Page<Product> products =  pR.findByCategory(category, page);

        products.forEach(data ->{
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(data, productDTO);
            productDTOS.add(productDTO);
        });

        return new PageImpl<>(productDTOS, page, products.getTotalElements());
    }

    public Page<ProductDTO> getProductForName(String name,Pageable page) {

        List<ProductDTO> productDTOS = new ArrayList<>();
        Page<Product> products = pR.findByNameOrCategoryOrDescriptionContaining(name, name, name, page);

        products.forEach(data ->{
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(data, productDTO);
            productDTOS.add(productDTO);
        });

        return new PageImpl<>(productDTOS, page, products.getTotalElements());

    }

    public Optional<Product> getProduct(Long id) {

            return (pR.findById(id));
    }

    @Transactional
    public Product saveProduct(Product products) {
        return pR.save(products);
    }

    @Transactional
    public Product editProduct(Product product, Long id) {

        var newProduct = product;
        newProduct.setId(id);

        return  pR.save(newProduct);

    }

    @Transactional
    public boolean salveEvaluation( EvaluationProductDTO evaluationProductDTO){
       var evaluationProduct = new EvaluationProduct();
       BeanUtils.copyProperties(evaluationProductDTO, evaluationProduct);

       try {
           if(pR.findById(evaluationProductDTO.getId_product()).isPresent()) {

               evaluationProduct.setProduct(pR.findById(evaluationProductDTO.getId_product()).get());
               DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
               evaluationProduct.setDate(date.format(LocalDateTime.now()));
               evaluationProduct.setUser(getUser());

               ePR.save(evaluationProduct);
               return true;

           }else {
               return false;

           }
       }catch (Exception e){
           System.out.println(e);
           return true;
       }


    }

    public List<EvaluationProductDTO> getEvaluations(Long productId){

        List<EvaluationProductDTO> evaluationProductDTOS = new ArrayList<>();

        ePR.findByProductId(productId).forEach(data -> {
            var evaluationDTO = new EvaluationProductDTO();
            BeanUtils.copyProperties(data, evaluationDTO);
            evaluationDTO.setUserName(data.getUser().getName());
            evaluationDTO.setId_product(data.getProduct().getId());
            evaluationProductDTOS.add(evaluationDTO);
        });

        return evaluationProductDTOS;
    }

    public List<EvaluationProductDTO> getEvaluationsByUser(){
        List<EvaluationProductDTO> evaluationProductDTOS = new ArrayList<>();

        ePR.findByUserEmail(getUser().getEmail()).forEach(data -> {
            var evaluationDTO = new EvaluationProductDTO();
            BeanUtils.copyProperties(data, evaluationDTO);
            evaluationDTO.setUserName(data.getUser().getName());
            evaluationDTO.setId_product(data.getProduct().getId());
            evaluationProductDTOS.add(evaluationDTO);
        });

        return evaluationProductDTOS;
    }

    @Transactional
    public boolean deleteEvaluation(Long id) {

        if(ePR.findById(id).isPresent() && ePR.findById(id).get().getUser().getEmail().equals(getUser().getEmail())){
            ePR.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public boolean deleteProduct(Long id) {

        Product product;

        if(pR.existsById(id)) {
            pR.deleteById(id);
            return true;
        }
        else {
            return false;
        }

    }

    private UserShopping getUser() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserShopping user = uR.findByEmail(auth.getName());

        return user;
    }
}
