package com.apiloja.service.product;

import com.apiloja.dto.productdto.ProductOfCartDTO;
import com.apiloja.model.products.ProductOfCart;
import com.apiloja.model.user.UserShopping;
import com.apiloja.repository.ordered.OrderedRepository;
import com.apiloja.repository.products.ProductOfCartRepository;
import com.apiloja.repository.products.ProductsRepository;
import com.apiloja.repository.user.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
public class ProductOfCartService {

    ProductsRepository pR;
    UsersRepository uR;
    OrderedRepository oR;
    ProductOfCartRepository pCR;

    public ProductOfCartService(ProductsRepository pR, UsersRepository uR, OrderedRepository oR, ProductOfCartRepository pCR) {
        this.pR = pR;
        this.uR = uR;
        this.oR = oR;
        this.pCR = pCR;
    }

    public List<ProductOfCartDTO> getShopingCart() {

        List<ProductOfCartDTO> productOfCartDTOS = new ArrayList<>();
        var productOfCarts = pCR.findByUserEmailAndInOrdered(getUser().getEmail(), false);

        productOfCarts.forEach(data ->{
            var productOfCartDTO = new ProductOfCartDTO();
            BeanUtils.copyProperties(data, productOfCartDTO);

            productOfCartDTO.setUser(data.getUser().getEmail());

            productOfCartDTOS.add(productOfCartDTO);
        });

        return productOfCartDTOS;

    }

    public ProductOfCartDTO postShopingCart(ProductOfCartDTO productOfCartDTO) {

        var newProductOfCart = new ProductOfCart();

        BeanUtils.copyProperties(productOfCartDTO, newProductOfCart);

        newProductOfCart.setUser(getUser());
        newProductOfCart.setTotal(productOfCartDTO.getProduct().getPrice() * productOfCartDTO.getQuantity());
        newProductOfCart.setInOrdered(false);

        var newProductOfCartDTO = new ProductOfCartDTO();
        BeanUtils.copyProperties(pCR.save(newProductOfCart), newProductOfCartDTO);
        pCR.save(newProductOfCart);

        return newProductOfCartDTO;

    }

    public Boolean deleteShopingCart(Long id){

        if(pCR.findById(id).isPresent()){
            if(pCR.findById(id).get().getUser().getEmail().equals(getUser().getEmail())){
                pCR.deleteById(id);
                return true;
            }else{
                return false;
            }
        }else {
            return false;

        }

    }

    @Transactional
    public boolean putShopingCart(Long id, ProductOfCartDTO productOfCartDTO) {

        var product = pR.findById(productOfCartDTO.getProduct().getId()).get();

        if(
                pCR.findById(id).isPresent() &&
                productOfCartDTO.getQuantity() <= product.getQuantity() &&
                pCR.findById(id).get().getUser().getEmail().equals(getUser().getEmail())
        ){
            var editProductOfCart = new ProductOfCart();

            BeanUtils.copyProperties(productOfCartDTO, editProductOfCart);

            editProductOfCart.setUser(getUser());
            editProductOfCart.setTotal(productOfCartDTO.getProduct().getPrice() * productOfCartDTO.getQuantity());
            editProductOfCart.setInOrdered(false);


           pCR.save(editProductOfCart);

            return true;
        }else {
            return false;
        }

    }


    private UserShopping getUser() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserShopping user = uR.findByEmail(auth.getName());

        return user;
    }



}
