package com.apiloja.service.ordered;

import com.apiloja.dto.ordereddto.OrderedDTO;
import com.apiloja.model.ordered.Ordered;
import com.apiloja.model.products.ProductOfCart;
import com.apiloja.model.user.UserShopping;
import com.apiloja.repository.ordered.OrderedRepository;
import com.apiloja.repository.products.ProductOfCartRepository;
import com.apiloja.repository.products.ProductsRepository;
import com.apiloja.repository.user.UsersRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderedService {
    ProductsRepository pR;
    UsersRepository uR;
    OrderedRepository oR;
    ProductOfCartRepository pCR;

    public OrderedService(ProductsRepository pR, UsersRepository uR, OrderedRepository oR, ProductOfCartRepository pCR) {
        this.pR = pR;
        this.uR = uR;
        this.oR = oR;
        this.pCR = pCR;
    }

    public List<OrderedDTO> orderedOfUser() {

        List<OrderedDTO> orderedDTOS = new ArrayList<>();
        var user = getUser();
        var ordereds = oR.findByUser(user);

        ordereds.forEach(data ->{
            var orderedDTO = new OrderedDTO();
            BeanUtils.copyProperties(data, orderedDTO);

            data.getProducts().forEach(productOfCart -> {
                productOfCart.getUser().setPassword("");
                orderedDTO.setProducts(productOfCart);
            });

            orderedDTO.setUser(user.getEmail());
            orderedDTOS.add(orderedDTO);
        });

        return orderedDTOS;
    }

    public Page<OrderedDTO> orderedAll(Pageable page) {

        List<OrderedDTO> orderedDTOS = new ArrayList<>();
        var ordereds = oR.findAll(page);

        ordereds.forEach(data ->{

            var orderedDTO = new OrderedDTO();
            BeanUtils.copyProperties(data, orderedDTO);

            data.getProducts().forEach(productOfCart -> {
                productOfCart.getUser().setPassword("");
                orderedDTO.setProducts(productOfCart);
            });

            orderedDTO.setUser(data.getUser().getEmail());
            orderedDTOS.add(orderedDTO);
        });

        return new PageImpl<>(orderedDTOS, page, ordereds.getTotalElements());
    }

    public Page<OrderedDTO> findOrderedByEmailUser(Pageable page, String email) {
        List<OrderedDTO> orderedDTOS = new ArrayList<>();
        var ordereds = oR.findByUserEmailContaining(email, page);

        ordereds.forEach(data ->{

            var orderedDTO = new OrderedDTO();
            BeanUtils.copyProperties(data, orderedDTO);

            data.getProducts().forEach(productOfCart -> {
                productOfCart.getUser().setPassword("");
                orderedDTO.setProducts(productOfCart);
            });

            orderedDTO.setUser(getUser().getEmail());
            orderedDTOS.add(orderedDTO);
        });

        return new PageImpl<>(orderedDTOS, page, ordereds.getTotalElements());

    }

    public boolean postOrdered(OrderedDTO orderedDTO) {

        double total = 0.0;
        UserShopping user = getUser();
        var ordered = new Ordered();

        BeanUtils.copyProperties(orderedDTO, ordered);

        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

        ordered.setUser(user);
        ordered.setDate(date.format(LocalDateTime.now()));

        List<ProductOfCart> products =  pCR.findByUserEmailAndInOrdered(user.getEmail(), false);

        for (ProductOfCart product : products) {
            total += product.getTotal();
            product.setInOrdered(true);
            pCR.save(product);
        }

        ordered.setTotal(total);

        products.forEach(ordered::setProducts);

        return oR.save(ordered) != null;


    }

    public boolean orderedPut(Long id, String status) {

        if(oR.findById(id).isPresent()){
            var ordered = oR.findById(id).get();

            ordered.setStatus(status);
            if (status.equals("Aprovado")) {
                ordered.getProducts().forEach(data -> {
                    var product = data.getProduct();
                    product.setQuantity(data.getProduct().getQuantity() - data.getQuantity());

                    if(product.getQuantity() <=0 )
                        product.setStatus("Indisponível");

                    pR.save(product);
                });
            }
            oR.save(ordered);
            return true;
        }else {
            return false;
        }

    }

    public boolean deleteOrdered(Long id) {

        if(oR.findById(id).isPresent()){
            var ordered = oR.findById(id).get();
            var products = ordered.getProducts();

            products.forEach(data -> {
                System.out.println(data.getProduct().getName());
            });

            oR.delete(ordered);

            products.forEach(data -> {
                pCR.deleteById(data.getId());
            });

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
